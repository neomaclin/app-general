package com.group.quasi.domain.persistence.operation

import com.group.quasi.domain.model.contents.messages.{Message, MessageBatch}
import com.group.quasi.domain.model.users.UserId


trait MessageRepository[F[_]] {

  def persist(roomId: String, userId: UserId, message: Message): F[Unit]

  def loadTailFrom(roomId:String, size:Int): F[MessageBatch]
}
