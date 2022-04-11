package com.group.quasi.domain.service

import com.group.quasi.domain.model.contents.Content
import com.group.quasi.domain.model.contents.messages.Message

trait ChatRoomMessageService[F[_]] {
  def post(inRoomId:String, byUserId:String, content: Content): F[Unit]
  def loadLatestBatch(roomId:String): F[List[Message]]

  //def
  //def streamFrom(roomId:String): F[List[Message]]
}
