package com.group.quasi.repository.user

import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.group.quasi.domain.model.users.ActivationKey
import com.group.quasi.domain.persistence.operation
import com.group.quasi.repository.{PostgresProfile, SlickRepository}

import java.time.Instant
import scala.concurrent.Future

final class ActivationKeyRepository(implicit override val session: SlickSession)
    extends SlickRepository
    with operation.ActivationKeyRepository[Future] {
  import PostgresProfile.api._

  private val activationKeys = TableQuery[ActivationKeys]
  class ActivationKeys(tag: Tag) extends Table[ActivationKey](tag, "activation_keys") {
    def id = column[Long]("user_id")
    def key = column[String]("key")
    def validUntil = column[Long]("valid_until")
    def * = (key, id, validUntil) <> (ActivationKey.tupled, ActivationKey.unapply)
  }
  def insert(key: String, userId: Long, validUntil: Instant): Future[Int] = {
    session.db.run(activationKeys+=ActivationKey(key,userId, validUntil = validUntil.toEpochMilli))
  }
  def delete(key: String, userId: Long): Future[Int] = {
    session.db.run(activationKeys.filter( keys=> keys.key === key && keys.id === userId).delete)
  }

  override def findByKey(key: String, now: Long): Future[Option[Long]] = {
    session.db.run(activationKeys.filter(keys => keys.validUntil > now && keys.key === key).map(_.id).result.headOption)
  }
}
