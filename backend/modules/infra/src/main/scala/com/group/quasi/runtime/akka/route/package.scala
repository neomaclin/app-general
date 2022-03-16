package com.group.quasi.runtime.akka

import com.group.quasi.domain.model.users.SuccessContent
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import pdi.jwt.{JwtClaim, JwtHeader}

import scala.concurrent.Future
import scala.util.Try

package object route {

  def checkClaims(
                   jwtToken: Try[(JwtHeader, JwtClaim, String)],
                 ): Future[Either[Unit, SuccessContent]] = {
    Future.successful(jwtToken.flatMap(_._2.content.asJson.as[SuccessContent].toTry).toEither.left.map(_ => ()))
  }
}
