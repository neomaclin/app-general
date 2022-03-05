package com.group.quasi.domain.model.contents

import cats.data.NonEmptyList

import java.time.Instant
import java.util.UUID

package object messages {

  final case class Message(id: UUID, participantId: String, content: Content, timestamp: Instant)
  final case class MessageBatch(roomId: String, messages: NonEmptyList[Message])

}
