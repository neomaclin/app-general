package com.group.quasi.service.impl

import cats.MonadThrow
import com.group.quasi.domain.service.BootstrapService
import com.group.quasi.repository.RepoMigration
import distage.TagK

import scala.concurrent.Future

class BootstrapServiceImpl[F[_]:MonadThrow:TagK](migration: RepoMigration) extends BootstrapService[F] {
  override def initialize(): F[Unit] = if (TagK[Future].hasPreciseClass) {
    migration.connectAndMigrate().asInstanceOf[F[Unit]]
  } else {
    MonadThrow[F].unit
  }
}
