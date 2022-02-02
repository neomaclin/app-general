package com.group.quasi.domain.infra

package object queue {
  sealed trait MessageBusOption
  sealed trait MessageBusConfig
  case object Kafka extends MessageBusOption
  case object AwsSQS extends MessageBusOption

  final case class MessageBusConfigs(currentOption: MessageBusOption, configs: Map[MessageBusOption, MessageBusConfig])

}
