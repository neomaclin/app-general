package exchange.api.spot

object TapirEndpoint {

  private val messageJsonBody: EndpointIO.Body[String, Message] =
    plainBody[Message]
      .description("message")
      .example(Message("ok"))


  private val createOrderRequestJsonBody: EndpointIO.Body[String, CreateOrderRequest] =
    jsonBody[CreateOrderRequest]
      .description("create order request")
      .example(CreateOrderRequest("348923", "btcusdt", "ButMarket", "349.3492", Option("9094.59090")))


  private val bearerAuthEndpoint: Endpoint[String, Unit, Message, Unit, Any] =
    endpoint
      .errorOut(messageJsonBody)
      .securityIn(auth.bearer[String]())

  val createOrderEndpoint: Endpoint[String, CreateOrderRequest, Message, Message, Any] =
    bearerAuthEndpoint
      .in("orders")
      .post
      .in(createOrderRequestJsonBody)
      .out(messageJsonBody)
}
