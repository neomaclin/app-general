package com.group.quasi.domain.service

import com.group.quasi.domain.model.users._

trait UserService[F[_]] {

  def register(user: String, password: String, email: String, phone: Option[String]): F[Either[Unit, String]]

  def activate(key: String): F[Either[ActivationFailure, ActivationSuccess]]

  def login(
      requestFrom: String,
      loginAs: Option[String],
      password: String,
      email: Option[String] = None,
      phone: Option[String] = None,
  ): F[Either[LoginFailure, LoginSuccess]]

  def updatePassword(loginAs: String, current: String, proposed: String): F[Either[Unit, Unit]]

}
