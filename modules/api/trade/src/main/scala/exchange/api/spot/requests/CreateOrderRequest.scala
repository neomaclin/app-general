package exchange.api.spot.requests

final case class CreateOrderRequest(
    account: String,
    symbol: String,
    orderType: String,
    amount: String,
    price: Option[String],
)
