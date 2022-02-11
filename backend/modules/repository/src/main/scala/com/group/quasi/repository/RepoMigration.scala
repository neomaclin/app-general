package com.group.quasi.repository

import akka.actor.typed.ActorSystem
import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.group.quasi.domain.persistence.repository.DBConfig
import org.flywaydb.core.Flyway

import scala.concurrent.{ExecutionContextExecutor, Future}

class RepoMigration(config: DBConfig)(implicit override val session: SlickSession, system: ActorSystem[Nothing])
    extends SlickRepository {
  import PostgresProfile.api._
  implicit val ec: ExecutionContextExecutor = system.executionContext

  def connectAndMigrate(): Future[Unit] = {
    for {
      _ <- migrate()
      _ <- testConnection()
    } yield ()
  }

  private val flyway = {
    Flyway
      .configure()
      .dataSource(config.url, config.username, config.password.value)
      .load()
  }

  private def migrate() = {
    if (config.migrateOnStart) Future(flyway.migrate()).map(_ => ()) else Future.unit
  }

  private def testConnection() =
    session.db.run(sql"""Select 1""".as[Int])

}
