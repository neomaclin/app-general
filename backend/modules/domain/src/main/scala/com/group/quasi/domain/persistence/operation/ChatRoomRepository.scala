package com.group.quasi.domain.persistence.operation

import com.group.quasi.domain.model.contents.messages.{Message, MessageBatch}
import com.group.quasi.domain.model.users.UserId


trait ChatRoomRepository[F[_]] {

  def register(roomId:String, creator:UserId, participants:List[UserId]): F[Unit]

  def loadBy(userId:UserId): F[List[String]]

  def append(userId:UserId, message: Message): F[List[String]]

  def loadTailFrom(roomId:String, size:Int): F[MessageBatch]
}
