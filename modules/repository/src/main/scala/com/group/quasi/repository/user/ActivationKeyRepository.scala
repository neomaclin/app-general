package com.group.quasi.repository.user

import akka.actor.ActorSystem
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{Sink, Source}
import com.group.quasi.domain.persistence.operation
import com.group.quasi.domain.persistence.repository.DBConfig
import com.group.quasi.repository.{PostgresProfile, SlickRepository}

import java.time.Instant
import scala.concurrent.Future

final class ActivationKeyRepository(override val config: DBConfig, postgresProfile: PostgresProfile)(implicit
    override val system: ActorSystem,
) extends SlickRepository
  with operation.ActivationKeyRepository[Future]{

  import postgresProfile.api._

  def insert(key: String, userId: Long, validUntil: Instant): Future[Int] = {
    Source
      .single(userId)
      .via(
        Slick.flow(userId => sqlu"""INSERT INTO activation_keys (key, user_id, valid_until) VALUES($key, $userId ,${validUntil.toEpochMilli})""")
      )
      .log("activation_keys-insert")
      .runWith(
        Sink.fold[Int, Int](0)(_ + _)
      )

  }
  def delete(key: String, userId: Long): Future[Int] = {
    Source
      .single(userId)
      .via(
        Slick.flow(userId => sqlu"""Delete from activation_keys WHERE key = $key user_id = $userId""")
      )
      .log("activation_keys-delete")
      .runWith(
        Sink.fold[Int, Int](0)(_ + _)
      )
  }

  override def findByKey(key: String): Future[Option[Long]] = {
    Slick.source(sql"select user_id from activation_keys WHERE key = $key".as[Long])
      .log("activation_keys-lookup")
      .runWith(Sink.headOption)
  }
}
