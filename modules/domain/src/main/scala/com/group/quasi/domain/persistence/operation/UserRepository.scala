package com.group.quasi.domain.persistence.operation

import com.group.quasi.domain.model.users.User

trait UserRepository[F[_]] {
  def insert(user: User): F[Int]
  def findById(id: Long): F[Option[User]]
  def findByEmail(email: String): F[Option[User]]
  def findByLogin(login: String): F[Option[User]]
  def findByLoginOrEmail(login: String, email: String): F[Option[User]]
  def updatePassword(userId: Long, newPassword: String): F[Int]
  def updateLogin(userId: Long, newLogin: String): F[Int]
  def updateEmail(userId: Long, newEmail: String): F[Int]
  def activate(userId: Long): F[Int]
}
