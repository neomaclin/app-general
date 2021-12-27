package exchange.domain.entities

import exchange.domain.vo.OrderStatusEnum.OrderStatus
import exchange.domain.vo.OrderTypeEnum.OrderType

final case class Order(
    accountId: String,
    orderId: String,
    symbol: String,
    orderStatus: OrderStatus,
    orderType: OrderType,
    amount: String,
    price: Option[String]
)
