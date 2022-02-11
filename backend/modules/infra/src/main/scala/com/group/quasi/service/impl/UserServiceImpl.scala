package com.group.quasi.service.impl

import cats.{Applicative, MonadThrow}
import com.group.quasi.domain.infra.notification.{NotificationData, NotificationSender}
import com.group.quasi.domain.model.roles.Member
import com.group.quasi.domain.model.users
import com.group.quasi.domain.model.users.{ActivationFailure, ActivationSuccess, User}
import com.group.quasi.domain.persistence.operation.{ActivationKeyRepository, LoginAttemptRepository, UserRepository}
import com.group.quasi.domain.service.UserService
import com.group.quasi.notification.email.EmailTemplates
import com.softwaremill.id.IdGenerator
import com.softwaremill.id.pretty.StringIdGenerator

import java.time.Instant
import java.time.temporal.ChronoUnit

class UserServiceImpl[F[_]: MonadThrow](
    userRepo: UserRepository[F],
    activationKeyRepository: ActivationKeyRepository[F],
    loginAttemptRepository: LoginAttemptRepository[F],
    keyGenerator: StringIdGenerator,
    idGenerator: IdGenerator,
    notificationSender: NotificationSender[F],
) extends UserService[F] {
  import cats.implicits._
  override def register(
      user: String,
      password: String,
      email: String,
      phone: Option[String],
  ): F[Either[Unit, String]] = {

    val result: F[Either[Unit, String]] =
      MonadThrow[F].ifM(userRepo.findByLoginOrEmail(user, email).map(_.isEmpty))(
        for {
          userId <- idGenerator.nextId().pure[F]
          activationKey <- keyGenerator.nextId().pure[F]
          activationKeyValidUntil <- Instant.now().plus(30, ChronoUnit.MINUTES).pure[F]
          _ <- userRepo.insert(
            User(id = userId, login = user, password = password, email = email, phone = phone, active = false),
          )
          _ <- activationKeyRepository.insert(activationKey, userId, activationKeyValidUntil)
          _ <- notificationSender.send(NotificationData.apply(email, EmailTemplates.activateTemplate()))
        } yield Right(activationKey),
        (Left(()): Either[Unit, String]).pure[F],
      )
    result.recover(_ => Left(()))
  }

  override def activate(key: String): F[Either[users.ActivationFailure, users.ActivationSuccess]] = {
    val result = for {
      users <- activationKeyRepository.findByKey(key)
      userOption <- users.traverse(userId => userRepo.findById(userId))
      _ <- userOption.flatten.traverse(user => userRepo.activate(user.id))
      _ <- userOption.flatten.traverse(user => activationKeyRepository.delete(key, user.id))
    } yield {
      userOption.flatten
        .map(user => ActivationSuccess(s"Registration for ${user.login} is now activated"))
        .toRight(ActivationFailure(s"Failed to activate for key: $key"))
    }
    result.recover(_ => Left(ActivationFailure(s"Failed to activate for key: $key")))
  }

  override def login(
      requestFrom: String,
      user: String,
      password: String,
      email: Option[String],
      phoneNumber: Option[String],
  ): F[Either[users.LoginFailure, users.LoginSuccess]] = {
    val result = for {
      userOption1 <- userRepo.findByLogin(user)
      userOption2 <- if (userOption1.isEmpty) userRepo.findByEmail(email.getOrElse("")) else None.pure[F]
      userOption3 <- if (userOption2.isEmpty) userRepo.findByPhoneNumber(phoneNumber.getOrElse("")) else None.pure[F]
      _ <- loginAttemptRepository.updateCount(requestFrom)
      countOption <- loginAttemptRepository.getCount(requestFrom)
    } yield {
      val userOption = userOption1.orElse(userOption2).orElse(userOption3)
      val counts = countOption.getOrElse(1)
      if (userOption.isEmpty && counts >= users.MAX_LOGIN_ATTEMPTS) {
        Left(users.LoginFailure(requestFrom, Instant.now(), counts))
      } else
        userOption
          .filter(_.password === password)
          .map(user => users.LoginSuccess(Instant.now(), users.SuccessContent(user.login, user.email, Member)))
          .toRight(users.LoginFailure(requestFrom, Instant.now(), counts))
    }
    result.recover(_ => Left(users.LoginFailure(requestFrom, Instant.now(), users.MAX_LOGIN_ATTEMPTS)))
  }

  override def logout(loginAs: String): F[Either[Unit, Unit]] = {
    Applicative[F].pure(Right(()))
  }

  override def updatePassword(loginAs: String, current: String, proposed: String): F[Either[Unit, Unit]] = {
    val result = for {
      userOption <- userRepo.findByLogin(loginAs)
      _ <- userOption.filter(_.password === current).traverse(user => userRepo.updatePassword(user.id, proposed))
    } yield {
      Right(()): Either[Unit, Unit]
    }
    result.recover(_ => Left(()))
  }
}
