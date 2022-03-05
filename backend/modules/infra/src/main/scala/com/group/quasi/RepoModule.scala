package com.group.quasi

import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.group.quasi.domain.persistence.{DBConfig, operation}
import com.group.quasi.repository.RepoMigration
import com.group.quasi.repository.notification.EmailRepository
import com.group.quasi.repository.user.{ActivationKeyRepository, LoginAttemptRepository, UserProfileRepository, UserRepository}
import distage.ModuleDef

import scala.concurrent.Future

class RepoModule extends ModuleDef {
  make[SlickSession].from { (config: DBConfig) =>
    SlickSession.forConfig("persistence." + config.profile)
  }
  make[operation.UserRepository[Future]].from[UserRepository]
  make[operation.LoginAttemptRepository[Future]].from[LoginAttemptRepository]
  make[operation.ActivationKeyRepository[Future]].from[ActivationKeyRepository]
  make[EmailRepository]
  make[UserProfileRepository]
  make[RepoMigration]
}
