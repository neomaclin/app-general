package com.group.quasi.service.impl

import com.group.quasi.repository.RepoMigration
import com.group.quasi.service.BootstrapService

import scala.concurrent.Future

class BootstrapServiceImpl(migration: RepoMigration) extends BootstrapService[Future] {
  override def initialize(): Future[Unit] = migration.connectAndMigrate()
}
