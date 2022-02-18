package com.group.quasi.domain.persistence.operation


trait LoginAttemptRepository[F[_]] {
  def updateCount(requestFrom: String): F[Option[Int]]
  def resetCount(requestFrom: String): F[Int]
}
