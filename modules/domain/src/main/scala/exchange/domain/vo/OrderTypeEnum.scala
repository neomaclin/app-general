package exchange.domain.vo

object OrderTypeEnum extends Enumeration {
  type OrderType = Value

  val ButMarket, SellMarket, BuyLimit, SellLimit = Value
}
