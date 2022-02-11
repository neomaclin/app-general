package com.group.quasi

import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.group.quasi.domain.persistence.repository.DBConfig
import com.group.quasi.repository.RepoMigration
import com.group.quasi.repository.notification.EmailRepository
import com.group.quasi.repository.user.{ActivationKeyRepository, UserProfileRepository, UserRepository}
import distage.ModuleDef

class RepoModule extends ModuleDef {
  make[SlickSession].from { (config: DBConfig) =>
    SlickSession.forConfig(config.profile)
  }
  make[UserRepository]
  make[UserProfileRepository]
  make[ActivationKeyRepository]
  make[EmailRepository]
  make[RepoMigration]
}
