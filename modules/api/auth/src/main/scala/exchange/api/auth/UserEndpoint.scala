package exchange.api.auth

import io.circe.generic.auto._
import pdi.jwt.{JwtCirce, JwtClaim}
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._

import scala.util.Try

object UserEndpoint {

  private val UserPath = "user"

  val register: Endpoint[Unit, RegisterRequest, RegisterFailure, RegisterResponse, Any] = endpoint.post
    .in(UserPath / "register")
    .in(jsonBody[RegisterRequest])
    .out(jsonBody[RegisterResponse])
    .errorOut(jsonBody[RegisterFailure])

  val login: Endpoint[Unit, LoginRequest, LoginFailure, LoginResponse, Any] = endpoint.post
    .in(UserPath / "login")
    .in(jsonBody[LoginRequest])
    .out(jsonBody[LoginResponse])
    .errorOut(jsonBody[LoginFailure])

  def activate: Endpoint[Unit, String, String, String, Any] = endpoint.put
    .in(UserPath / "activate")
    .in(query[String]("key"))
    .out(statusCode(StatusCode.Accepted) and stringBody)
    .errorOut(statusCode(StatusCode.NotFound) and stringBody)

  class SecuredUserEndpoint(jwtConfig: JwtConfig) {

    private val jwt =
      auth
        .bearer[String]()
        .map(token => JwtCirce.decode(token, jwtConfig.publicKey, Seq(jwtConfig.algo)))(
          _.map(claim => JwtCirce.encode(claim, jwtConfig.privateKey, jwtConfig.algo)).getOrElse(""),
        )

    private val securedEndpoint = endpoint
      .securityIn(jwt)

    val logout: Endpoint[Try[JwtClaim], Unit, Unit, Unit, Any] = securedEndpoint.delete
      .in(UserPath)

    val changePassword
        : Endpoint[Try[JwtClaim], PasswordResetRequest, PasswordResetFailure, PasswordResetResponse, Any] =
      securedEndpoint.post
        .in(UserPath / "changepassword")
        .in(jsonBody[PasswordResetRequest])
        .out(jsonBody[PasswordResetResponse])
        .errorOut(jsonBody[PasswordResetFailure])

  }
}
