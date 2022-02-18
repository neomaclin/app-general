package com.group.quasi.repository.user

import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.group.quasi.domain.persistence.operation
import com.group.quasi.repository.{PostgresProfile, SlickRepository}

import java.time.Instant
import scala.concurrent.Future

final class ActivationKeyRepository(implicit override val session: SlickSession)
    extends SlickRepository
    with operation.ActivationKeyRepository[Future] {
  import PostgresProfile.api._

  def insert(key: String, userId: Long, validUntil: Instant): Future[Int] = {
    session.db.run(
      sqlu"""INSERT INTO activation_keys (key, user_id, valid_until) VALUES ($key, $userId ,${validUntil.toEpochMilli})""",
    )
  }
  def delete(key: String, userId: Long): Future[Int] = {
    session.db.run(sqlu"""Delete from activation_keys WHERE key = $key user_id = $userId""")
  }

  override def findByKey(key: String, now: Long): Future[Option[Long]] = {
    session.db.run(sql"select user_id from activation_keys WHERE valid_until > $now and key = $key".as[Long].headOption)
  }
}
