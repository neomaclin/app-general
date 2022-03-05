package com.group.quasi.app.api

import com.group.quasi.domain.model.contents.Content

import java.time.Instant


package object chat {
  final case class ChatMessageBatch(lastMessage: Content, trails: List[Content])
  final case class MessageRequest(content:Content)
  final case class ChatRoom(id:String, name:String, host:String, participants:List[String])
  final case class ChatRooms(list:List[ChatRoom])
  final case class PostedMessage(id: String, byUser:String, content: Content, posted: Instant)
  final case class Messages(messages:List[PostedMessage])
}
