package com.group.quasi.domain.persistence.operation



import java.time.Instant

trait ActivationKeyRepository[F[_]] {
  def insert(key: String, userId: Long, validUntil: Instant): F[Int]
  def delete(key: String, userId: Long): F[Int]
  def findByKey(key: String): F[Option[Long]]
}
