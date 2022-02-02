package exchange.api.spot.models

object OrderTypeEnum extends Enumeration {
  type OrderType = Value

  val ButMarket, SellMarket, BuyLimit, SellLimit = Value
}
