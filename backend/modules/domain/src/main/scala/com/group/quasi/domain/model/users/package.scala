package com.group.quasi.domain.model

import com.group.quasi.domain.model.roles.Role

import eu.timepit.refined.auto._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive

import java.time.Instant
import scala.concurrent.duration.{DurationInt, FiniteDuration}

package object users {

  val MAX_LOGIN_ATTEMPTS: Int Refined Positive  = 5
  val MAX_ACTIVATION_WINDOW: FiniteDuration = 3.hours

  final case class UserConfig(
      loginMaxAttempts: Int = MAX_LOGIN_ATTEMPTS.value,
      activationWindow: FiniteDuration = MAX_ACTIVATION_WINDOW,
  )

  type UserId = Long
  final case class User(
      id: UserId,
      login: String,
      email: String,
      password: String,
      salt: String,
      phone: Option[String],
      nodeTime: Long,
      active: Boolean,
  )

  final case class UserProfile(
      userId: UserId,
      lastName: String,
      firstName: String,
      alsoKnowAs: String,
      preferredContact: String,
      gender: String,
      snAccounts: List[String],
      updatedOn: Option[Long],
      memo: String,
  )

  final case class ActivationKey(key: String, userId: UserId, validUntil: Long)

  final case class ActivationFailure(msg: String)

  final case class ActivationSuccess(msg: String)

  sealed trait LoginFailure

  final case class MaxAttemptReached(requestFrom: String) extends LoginFailure

  final case class LoginAttemptFailure(requestFrom: String, attemptedAt: Instant, attemptCount: Int) extends LoginFailure

  final case class SuccessContent(user: String, email: String, role: Role)

  final case class LoginSuccess(loginAt: Instant, content: SuccessContent)

}
