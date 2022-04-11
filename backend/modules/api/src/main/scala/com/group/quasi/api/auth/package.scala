package com.group.quasi.api

import com.group.quasi.domain.model.users
import com.group.quasi.domain.model.users.{LoginSuccess, UserConfig}
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.And
import eu.timepit.refined.collection.{MaxSize, MinSize}
import eu.timepit.refined.string.MatchesRegex
import io.circe.syntax._
import io.circe.generic.auto._
import pdi.jwt.algorithms.JwtAsymmetricAlgorithm
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim, JwtHeader}
import shapeless.Witness

import java.security.{PrivateKey, PublicKey}

package object auth {

  type LoginAlias = String
  type Email = String
  type Phone = String
  type Password = String
  val email =
    Witness(
      """^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""",
    )
  type Regex = email.T

  type RefinedEmail = Email Refined MatchesRegex[Regex]
  type RefinedAlias = LoginAlias Refined And[MinSize[W.`5`.T], MaxSize[W.`25`.T]]
  type RefinedPhone = Phone Refined And[MinSize[W.`8`.T], MaxSize[W.`18`.T]]
  type RefinedPassword = Password Refined And[MinSize[W.`8`.T], MaxSize[W.`25`.T]]

  final case class RegisterRequest(
      login: RefinedAlias,
      password: RefinedPassword,
      email: RefinedEmail,
      phone: Option[RefinedPhone],
  )

  final case class RegisterResponse(
      activationKey: String,
      keyValidDuration: String,
  )

  final case class RegisterFailure(
      msg: String,
  )

  final case class LoginRequest(
      login: Option[RefinedAlias],
      password: RefinedPassword,
      email: Option[RefinedEmail],
      phone: Option[RefinedPhone],
  )

  final case class LoginResponse(accessToken: String)

  object LoginResponse {
    def from(success: LoginSuccess, jwtConfig: JwtConfig): LoginResponse = {
      val claim = JwtClaim.apply(
        content = success.content.asJson.noSpaces,
        issuedAt = Some(success.loginAt.toEpochMilli),
        // expiredAt =
      )
      val header = JwtHeader.apply(
        jwtConfig.algo,
      )

      LoginResponse(JwtCirce.encode(header, claim, jwtConfig.privateKey))
    }
  }

  final case class LoginFailure(attemptCounts: Int, accountLocked: Boolean)

  object LoginFailure {
    def from(failure: users.LoginFailure, config: UserConfig): LoginFailure = failure match {
      case users.MaxAttemptReached(_)                    => LoginFailure(config.loginMaxAttempts, accountLocked = true)
      case users.LoginAttemptFailure(_, _, attemptCount) => LoginFailure(attemptCount, accountLocked = false)
    }
  }

  final case class PasswordResetRequest(current: Password, proposed: Password)

  final case class PasswordResetResponse(msg: String)

  final case class JwtConfig(
      publicKey: PublicKey,
      privateKey: PrivateKey,
      algo: JwtAsymmetricAlgorithm = JwtAlgorithm.Ed25519,
  )

  final case class UserProfile(
      lastName: String,
      firstName: String,
      aka: String,
      preferredContact: String,
      gender: String,
      snAccounts: List[String],
      memo: String,
  )
}
