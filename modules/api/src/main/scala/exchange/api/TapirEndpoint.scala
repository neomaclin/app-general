package exchange.api

import exchange.api.requests.CreateOrderRequest
import exchange.domain.entity.{Account, Token}
import exchange.domain.vo.{Message, OrderTypeEnum}
import io.circe.generic.auto._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.model.UsernamePassword
import sttp.tapir.{EndpointIO, _}

object TapirEndpoint {
  private val accountsJsonBody: EndpointIO.Body[String, Seq[Account]] =
    jsonBody[Seq[Account]]
      .description("accounts")
      .example(Seq(Account("10323", "348989.3489")))

  private val messageJsonBody: EndpointIO.Body[String, Message] =
    jsonBody[Message]
      .description("message")
      .example(Message("ok"))

  private val tokenJsonBody: EndpointIO.Body[String, Token] =
    jsonBody[Token]
      .description("jwt")
      .example(
        Token(
          "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
            ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ" +
            ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
        ),
      )

  private val createOrderRequestJsonBody: EndpointIO.Body[String, CreateOrderRequest] =
    jsonBody[CreateOrderRequest]
      .description("create order request")
      .example(CreateOrderRequest("348923", "btcusdt", "ButMarket", "349.3492", Option("9094.59090")))

  private val basicAuthEndpoint: Endpoint[UsernamePassword, Unit, Message, Unit, Any] =
    endpoint
      .errorOut(messageJsonBody)
      .securityIn(auth.basic[UsernamePassword]())

  private val bearerAuthEndpoint: Endpoint[String, Unit, Message, Unit, Any] =
    endpoint
      .errorOut(messageJsonBody)
      .securityIn(auth.bearer[String]())

  val createTokenEndpoint: Endpoint[UsernamePassword, Unit, Message, Token, Any] =
    basicAuthEndpoint
      .in("tokens")
      .post
      .out(tokenJsonBody)

  val createOrderEndpoint: Endpoint[String, CreateOrderRequest, Message, Message, Any] =
    bearerAuthEndpoint
      .in("orders")
      .post
      .in(createOrderRequestJsonBody)
      .out(messageJsonBody)
}
