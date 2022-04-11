package com.group.quasi.domain.persistence.operation

import com.group.quasi.domain.model.users.UserProfile


trait UserProfileRepository[F[_]] {
  def insertOrUpdate(profile: UserProfile): F[Int]
  def lookupBy(userId: Long): F[Option[UserProfile]]

}
