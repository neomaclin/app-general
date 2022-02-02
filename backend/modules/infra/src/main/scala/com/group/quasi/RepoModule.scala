package com.group.quasi

import com.group.quasi.repository.RepoMigration
import com.group.quasi.repository.notification.EmailRepository
import com.group.quasi.repository.user.{ActivationKeyRepository, UserProfileRepository, UserRepository}
import distage.ModuleDef

class RepoModule extends ModuleDef {
  make[UserRepository]
  make[UserProfileRepository]
  make[ActivationKeyRepository]
  make[EmailRepository]
  make[RepoMigration]
}
