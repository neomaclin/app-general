package com.group.quasi.api

import java.time.ZoneId
import java.util.TimeZone

package object heartbeat {

  sealed trait NodeStatus
  case object Fine extends NodeStatus
  case object Busy extends NodeStatus
  case object WarmingUp extends NodeStatus

  final case class Pong(status: NodeStatus = Fine,
                        nodeId: String = "1",
                        serviceVersion: String = "v1",
                        timezone: ZoneId = TimeZone.getDefault.toZoneId)

}
