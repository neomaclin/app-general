package com.github.scalacn.exchange.api

import com.github.scalacn.exchange.domain.{Account, Message, Token}
import io.circe.generic.auto._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.model.UsernamePassword
import sttp.tapir.{EndpointIO, _}

object TapirEndpoint {
  val accountsJsonBody: EndpointIO.Body[String, Seq[Account]] =
    jsonBody[Seq[Account]]
      .description("accounts")
      .example(Seq(Account("10323", "348989.3489")))

  val messageJsonBody: EndpointIO.Body[String, Message] = jsonBody[Message]
    .description("message")
    .example(Message("ok"))

  val tokenJsonBody: EndpointIO.Body[String, Token] = jsonBody[Token]
    .description("jwt")
    .example(
      Token(
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
          ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ" +
          ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
      )
    )

  val basicAuthEndpoint: Endpoint[UsernamePassword, Unit, Message, Unit, Any] =
    endpoint
      .errorOut(messageJsonBody)
      .securityIn(auth.basic[UsernamePassword]())

  val createTokenEndpoint: Endpoint[UsernamePassword, Unit, Message, Token, Any] =
    basicAuthEndpoint
      .in("tokens")
      .post
      .out(tokenJsonBody)
}
