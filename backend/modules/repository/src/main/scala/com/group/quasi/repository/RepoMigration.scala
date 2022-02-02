package com.group.quasi.repository

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.group.quasi.domain.persistence.repository.DBConfig
import org.flywaydb.core.Flyway

import scala.concurrent.Future

class RepoMigration(override val config: DBConfig, val postgresProfile: PostgresProfile)(implicit override val system: ActorSystem)
  extends SlickRepository{
  import postgresProfile.api._

  def connectAndMigrate(): Future[Unit] = {
    migrate()
      .via(testConnection())
      .log("db-migration")
       .runWith(Sink.ignore.mapMaterializedValue(_=>Future.unit))
  }

  private val flyway = {
    Flyway
      .configure()
      .dataSource(config.url, config.username, config.password.value)
      .load()
  }

  private def migrate(): Source[Unit,NotUsed] = {
    if (config.migrateOnStart) Source.future(Future.successful(flyway.migrate()))
    else Source.empty[Unit]
  }

  private def testConnection():Flow[Unit,Int,NotUsed]  =
    Flow.fromSinkAndSourceCoupled(Sink.ignore, Slick.source(sql"""Select 1""".as[Int]))


}
