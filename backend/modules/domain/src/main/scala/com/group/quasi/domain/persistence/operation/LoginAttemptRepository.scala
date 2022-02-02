package com.group.quasi.domain.persistence.operation



trait LoginAttemptRepository[F[_]] {
  def updateCount(requestFrom: String): F[Int]
  def getCount(requestFrom: String): F[Option[Int]]
}
