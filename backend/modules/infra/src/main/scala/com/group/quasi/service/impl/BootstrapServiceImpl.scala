package com.group.quasi.service.impl

import com.group.quasi.domain.service.BootstrapService
import com.group.quasi.repository.RepoMigration

import scala.concurrent.Future

class BootstrapServiceImpl(migration: RepoMigration) extends BootstrapService[Future] {
  override def initialize(): Future[Unit] = migration.connectAndMigrate()
}
