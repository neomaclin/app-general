package com.group.quasi.domain.service

import com.group.quasi.domain.model.users._

trait UserService[F[_]] {

  def register(user: String, password: String, email: String, phone: Option[String]): F[Either[Unit,String]]

  def activate(key:String): F[Either[ActivationFailure, ActivationSuccess]]

//  def login(user: String, password: String, email: Option[String] = None, phone: Option[String] = None): F[Either[LoginFailure,LoginSuccess]]
////
  def lookup(user: SuccessContent): F[Either[Unit,Option[User]]]
//
  def logout(user: User): F[Either[Unit,Unit]]
//
//  def updatePassword(user: User, current: String, proposed:String): F[Either[Unit,Unit]]

}
