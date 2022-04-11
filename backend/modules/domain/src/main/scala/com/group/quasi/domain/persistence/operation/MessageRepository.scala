package com.group.quasi.domain.persistence.operation

import com.group.quasi.domain.model.contents.messages.Message

trait MessageRepository[F[_]] {

  def persist(roomId: String, userId: String, message: Message): F[Unit]

  def loadLatestBatch(roomId: String): F[List[Message]]
}
