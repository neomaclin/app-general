package com.group.quasi.domain.model

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
      memo: String,
  )

  final case class ActivationFailure(msg: String)
  final case class ActivationSuccess(msg: String)
  final case class LoginFailure(attemptedAt: Instant, attemptCount: Int)
  final case class SuccessContent(
      user: String,
      role: String,
  )

  final case class LoginSuccess(loginAt: Instant, content: SuccessContent)

//  final case class LogoutFailure(loginAt:Instant, asRole: String)
}
