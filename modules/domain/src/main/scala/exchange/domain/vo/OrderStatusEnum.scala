package exchange.domain.vo

object OrderStatusEnum extends Enumeration {
  type OrderStatus = Value

  val Created, Submitted, PartialFilled, Filled, PartialCanceled, Canceling, Canceled = Value
}
