package exchange.api

import com.group.quasi.domain.model.users
import com.group.quasi.domain.model.users.{LoginSuccess, UserConfig}
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean._
import eu.timepit.refined.collection._
import eu.timepit.refined.string.MatchesRegex
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import pdi.jwt.algorithms.JwtAsymmetricAlgorithm
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}
import shapeless.Witness

import java.security.{PrivateKey, PublicKey}

package object auth {

  val email =
    Witness("""^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""")

  type Regex = email.T
  type LoginAlias = String
  type Email = String
  type Phone = String
  type Password = String

  type RefinedEmail = Email Refined MatchesRegex[Regex]
  type RefinedAlias = LoginAlias Refined MinSize[W.`5`.T] And MaxSize[W.`25`.T]
  type RefinedPhone = Phone Refined MinSize[W.`8`.T] And MaxSize[W.`18`.T]
  type RefinedPassword = LoginAlias Refined MinSize[W.`8`.T] And MaxSize[W.`25`.T]

  final case class RegisterRequest(
      login: LoginAlias,
      password: Password,
      email: Email,
      phone: Option[Phone],
  )

  final case class RegisterResponse(
      activationKey: String,
      keyValidDuration: String,
  )

  final case class RegisterFailure(
      msg: String,
  )

  final case class LoginRequest(
      login: LoginAlias,
      password: Password,
  )

  final case class LoginResponse(
      jwtToken: String,
  )
  object LoginResponse {
    def from(success: LoginSuccess, jwtConfig: JwtConfig): LoginResponse = {
      val claim = JwtClaim(content = success.content.asJson.noSpaces, issuedAt = Some(success.loginAt.toEpochMilli))
      LoginResponse(JwtCirce.encode(claim, jwtConfig.privateKey, jwtConfig.algo))
    }
  }
  final case class LoginFailure(attemptCounts: Int, accountLocked: Boolean)
  object LoginFailure {
    def from(failure: users.LoginFailure, config: UserConfig): LoginFailure = {
      LoginFailure(failure.attemptCount, failure.attemptCount >= config.loginMaxAttempts)
    }
  }
  final case class PasswordResetRequest(current: Password, proposed: Password)

  final case class PasswordResetResponse()

  final case class PasswordResetFailure()

  final case class JwtConfig(
      publicKey: PublicKey,
      privateKey: PrivateKey,
      secretKey: String,
      algo: JwtAsymmetricAlgorithm = JwtAlgorithm.Ed25519,
  )

}
