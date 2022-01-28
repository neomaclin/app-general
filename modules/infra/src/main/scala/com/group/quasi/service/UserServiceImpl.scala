package com.group.quasi.service

import cats.{Applicative, MonadThrow}
import com.group.quasi.domain.infra.notification.{NotificationData, NotificationSender}
import com.group.quasi.domain.model.users
import com.group.quasi.domain.model.users.{ActivationFailure, ActivationSuccess, User}
import com.group.quasi.domain.persistence.operation.{ActivationKeyRepository, UserRepository}
import com.group.quasi.domain.service.UserService
import com.group.quasi.notification.email.EmailTemplates
import com.softwaremill.id.IdGenerator
import com.softwaremill.id.pretty.StringIdGenerator

import java.time.Instant
import java.time.temporal.ChronoUnit


class UserServiceImpl[F[_]: MonadThrow](
    userRepo: UserRepository[F],
    activationKeyRepository: ActivationKeyRepository[F],
    keyGenerator:StringIdGenerator,
    idGenerator: IdGenerator,
    notificationSender: NotificationSender[F]
) extends UserService[F] {
  import cats.implicits._
  override def register(
      user: String,
      password: String,
      email: String,
      phone: Option[String],
  ): F[Either[Unit, String]] = {

    val result: F[Either[Unit, String]] = for {
      userId <-idGenerator.nextId().pure[F]
      activationKey <-  keyGenerator.nextId().pure[F]
      activationKeyValidUntil <- Instant.now().plus(30, ChronoUnit.MINUTES).pure[F]
      _ <- MonadThrow[F].ifM(userRepo.findByLoginOrEmail(user, email).map(_.isEmpty))(
        for {
          _ <- userRepo.insert(User(id = userId, login = user, password = password, email = email, phone = phone))
          _ <- activationKeyRepository.insert(activationKey, userId, activationKeyValidUntil)
          _ <- notificationSender.send(NotificationData.apply(email,EmailTemplates.activateTemplate()))
        } yield ()
        ,
        ().pure[F]
      )
    } yield {
      Right(activationKey)
    }

    result.recover(_ => Left(()))
  }

  override def activate(key: String): F[Either[users.ActivationFailure, users.ActivationSuccess]] = {
    val result: F[Either[users.ActivationFailure, users.ActivationSuccess]] = for {
      userIdOption <- activationKeyRepository.findByKey(key)
      userOption <- userIdOption.traverse(userId => userRepo.findById(userId))
      _ <- userOption.flatten.traverse(user => userRepo.activate(user.id))
      _ <- userOption.flatten.traverse(user => activationKeyRepository.delete(key, user.id))
    } yield {
      userOption.flatten
        .map(user => ActivationSuccess(s"Registration for ${user.login} is now activated"))
        .toRight(ActivationFailure(s"Failed to activate for key: $key"))
    }
    result.recover(_ => Left(ActivationFailure(s"Failed to activate for key: $key")))
  }

//  override def login(user: String, password: String, email: Option[String], phone: Option[String]): F[Either[users.LoginFailure, users.LoginSuccess]] = {
//    //Applicative[F].pure(Right(LoginSuccess(Instant.now,SuccessContent("",""))))
//
//  }

  override def lookup(user: users.SuccessContent): F[Either[Unit, Option[users.User]]] = {
    Applicative[F].pure(Right(None))
  }
//  }
//
  override def logout(user: users.User): F[Either[Unit, Unit]] = {
    Applicative[F].pure(Right(()))
  }
//
//  override def updatePassword(user: users.User, current: String, proposed: String): F[Either[Unit, Unit]] = {
//    Applicative[F].pure(Right(()))
//  }

}
