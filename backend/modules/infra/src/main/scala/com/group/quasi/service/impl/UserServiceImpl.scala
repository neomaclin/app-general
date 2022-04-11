package com.group.quasi.service.impl

import cats.data.OptionT
import cats.{Applicative, MonadThrow}
import com.group.quasi.domain.infra.notification.NotificationDataBuilder
import com.group.quasi.domain.model.roles.Member
import com.group.quasi.domain.model.users
import com.group.quasi.domain.model.users.{ActivationFailure, ActivationSuccess, User, UserProfile}
import com.group.quasi.domain.persistence.operation.{ActivationKeyRepository, LoginAttemptRepository, UserProfileRepository, UserRepository}
import com.group.quasi.domain.service.UserService
import com.group.quasi.notification.email.EmailTemplates
import com.group.quasi.service.NotificationService
import com.softwaremill.id.IdGenerator
import com.softwaremill.id.pretty.StringIdGenerator
import izumi.functional.mono.Clock
import org.mindrot.jbcrypt.BCrypt

import java.time.Instant
import java.time.temporal.ChronoUnit
import scala.util.control.NonFatal

class UserServiceImpl[F[_]: MonadThrow: Clock](
    userRepo: UserRepository[F],
    userProfileRepo: UserProfileRepository[F],
    activationKeyRepository: ActivationKeyRepository[F],
    loginAttemptRepository: LoginAttemptRepository[F],
    keyGenerator: StringIdGenerator,
    idGenerator: IdGenerator,
    notificationService: NotificationService[F],
) extends UserService[F] {
  import cats.implicits._
  override def register(
      user: String,
      password: String,
      email: String,
      phone: Option[String],
  ): F[Either[Throwable, String]] = {
    MonadThrow[F].ifM(userRepo.findByLoginOrEmail(user, email).map(result => result.isEmpty))(
      (for {
        userId <- MonadThrow[F].pure(idGenerator.nextId())
        activationKey <- MonadThrow[F].pure(keyGenerator.nextId())
        activationKeyValidUntil <- Clock[F].now().map(_.plus(30, ChronoUnit.MINUTES)).map(_.toInstant)
        salt <- Applicative[F].pure(BCrypt.gensalt())
        passwordHash <- Applicative[F].pure(BCrypt.hashpw(password, salt))
        now <- Clock[F].now().map(_.toInstant.toEpochMilli)
        _ <- userRepo.insert(
          User(
            id = userId,
            login = user,
            password = passwordHash,
            salt = salt,
            email = email,
            phone = phone.filter(_.nonEmpty),
            nodeTime = now,
            active = false,
          ),
        )
        _ <- activationKeyRepository.insert(activationKey, userId, activationKeyValidUntil)
        _ <- notificationService.send(
          NotificationDataBuilder.from(email, EmailTemplates.activateTemplate("Activation Key", activationKey)),
        )
      } yield Right(activationKey): Either[Throwable, String]).recover { case NonFatal(e) =>
        Left(e)
      },
      Applicative[F].pure[Either[Throwable, String]](Left(new RuntimeException("User already existed."))),
    )
  }

  override def activate(key: String): F[Either[users.ActivationFailure, users.ActivationSuccess]] = {
    (for {
      now <- OptionT.liftF(Clock[F].now().map(_.toInstant.toEpochMilli))
      userId <- OptionT(activationKeyRepository.findByKey(key, now))
      _ <- OptionT.liftF(userRepo.activate(userId))
      _ <- OptionT.liftF(activationKeyRepository.delete(key, userId))
      user <- OptionT(userRepo.findById(userId))
    } yield {
      ActivationSuccess(s"Registration for ${user.login} is now activated")
    }).toRight(ActivationFailure(s"Failed to activate for key: $key")).value
  }

  override def login(
      requestFrom: String,
      user: Option[String],
      password: String,
      email: Option[String],
      phoneNumber: Option[String],
  ): F[Either[users.LoginFailure, users.LoginSuccess]] = {
    OptionT(loginAttemptRepository.updateCount(requestFrom))
      .filter(_ <= users.MAX_LOGIN_ATTEMPTS)
      .foldF(
        Applicative[F].pure[Either[users.LoginFailure, users.LoginSuccess]](Left(users.MaxAttemptReached(requestFrom))),
      ) { count =>
        (for {
          user <- OptionT(user.filter(_.trim.nonEmpty).flatTraverse(userRepo.findByLogin))
            .orElse(OptionT(email.filter(_.trim.nonEmpty).flatTraverse(userRepo.findByEmail)))
            .orElse(OptionT(phoneNumber.filter(_.trim.nonEmpty).flatTraverse(userRepo.findByPhoneNumber)))
          if BCrypt.hashpw(password, user.salt) === user.password
          _ <- OptionT.liftF(loginAttemptRepository.resetCount(requestFrom))
        } yield {
          users.LoginSuccess(Instant.now(), users.SuccessContent(user.login, user.email, Member))
        }).toRight[users.LoginFailure](users.LoginAttemptFailure(requestFrom, Instant.now(), count)).value
      }
  }

  override def updatePassword(loginAs: String, current: String, proposed: String): F[Either[Unit, Int]] = {
    (for {
      user <- OptionT(userRepo.findByLogin(loginAs)) if user.password === current
      count <- OptionT.liftF(userRepo.updatePassword(user.id, proposed))
    } yield count).value.map(_.toRight(Left(())))
  }

  override def createOrUpdateUserProfile(
      user: String,
      lastName: String,
      firstName: String,
      alsoKnowAs: String,
      preferredContact: String,
      gender: String,
      snAccounts: List[String],
      memo: String,
  ): F[Either[Unit, Int]] = {
    (for {
      user <- OptionT(userRepo.findByLogin(user))
      count <- OptionT(userProfileRepo.insertOrUpdate(UserProfile(
          userId = user.id,
          lastName = lastName,
          firstName= firstName,
          alsoKnowAs = alsoKnowAs,
          preferredContact = preferredContact,
          gender = gender,
          snAccounts = snAccounts,
          memo = memo,
          updatedOn = Some(Instant.now().toEpochMilli))).map(Option(_)))
    } yield count).value.map(_.toRight(Left(())))
  }

  override def getUserProfile(user: String): F[Either[Unit, UserProfile]] =
    (for {
      user <- OptionT(userRepo.findByLogin(user))
      profile <- OptionT(userProfileRepo.lookupBy(user.id))
    } yield profile).value.map(_.toRight(Left(())))

}
