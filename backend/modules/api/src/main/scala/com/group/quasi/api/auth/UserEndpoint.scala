package com.group.quasi.api.auth

import io.circe.generic.auto._
import pdi.jwt.{JwtCirce, JwtClaim, JwtHeader}
import sttp.model.StatusCode
import io.circe.refined._
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.codec.refined._
import scala.util.Try

object UserEndpoint {

  val UserPath = "user"

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
        .map(token => JwtCirce.decodeAll(token, jwtConfig.publicKey, Seq(jwtConfig.algo)))(_ => "")

    val securedEndpoint = endpoint
      .securityIn(jwt)

    val changePassword: Endpoint[Try[
      (JwtHeader, JwtClaim, String),
    ], PasswordResetRequest, Unit, PasswordResetResponse, Any] =
      securedEndpoint.post
        .in(UserPath / "changepassword")
        .in(jsonBody[PasswordResetRequest])
        .out(jsonBody[PasswordResetResponse])

  }
}
