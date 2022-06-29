package com.group.quasi.domain.model.contents

import cats.data.NonEmptyList
import com.group.quasi.domain.model.users.UserId

import java.time.Instant
import java.util.UUID

package object messages {

  final case class Message(id: UUID, participantId: UserId, content: Content, timestamp: Instant)
  final case class MessageBatch(roomId: String, messages: NonEmptyList[Message])

}
