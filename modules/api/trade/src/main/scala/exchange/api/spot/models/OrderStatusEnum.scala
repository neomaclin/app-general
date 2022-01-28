package exchange.api.spot.models

object OrderStatusEnum extends Enumeration {
  type OrderStatus = Value

  val Created, Submitted, PartialFilled, Filled, PartialCanceled, Canceling, Canceled = Value
}
