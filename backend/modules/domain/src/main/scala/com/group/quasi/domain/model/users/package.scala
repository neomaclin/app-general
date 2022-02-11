package com.group.quasi.domain.model

import com.group.quasi.domain.model.roles.Role

import java.time.Instant
import scala.concurrent.duration.{DurationInt, FiniteDuration}

package object users {
  val MAX_LOGIN_ATTEMPTS = 5
  val MAX_ACTIVATION_WINDOW: FiniteDuration = 3.hours

  final case class UserConfig(
      loginMaxAttempts: Int = MAX_LOGIN_ATTEMPTS,
      activationWindow: FiniteDuration = MAX_ACTIVATION_WINDOW,
  )

  final case class User(
      id: Long,
      login: String,
      email: String,
      password: String,
      phone: Option[String],
      active: Boolean,
  )

  final case class UserProfile(
      id: Long,
      userId: Long,
      lastName: String,
      firstName: String,
      alsoKnowAs: String,
      preferredContact: String,
      gender: String,
      snAccounts: List[String],
      updatedOn: Instant,
      memo: String,
  )

  final case class ActivationFailure(msg: String)

  final case class ActivationSuccess(msg: String)

  final case class LoginFailure(requestFrom: String, attemptedAt: Instant, attemptCount: Int)

  final case class SuccessContent(loginAs: String, email: String, role: Role)

  final case class LoginSuccess(loginAt: Instant, content: SuccessContent)

}
