package com.group.quasi.domain.model.chat

import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext}
import akka.actor.typed.{ActorRef, Behavior}
import com.group.quasi.domain.model.contents.Content
import com.group.quasi.domain.model.contents.messages.Message
import com.group.quasi.domain.persistence.operation.ChatRoomRepository

import java.time.Instant
import java.util.UUID
import scala.collection.mutable
import scala.concurrent.Future

object Chatroom {

  sealed trait Action

  final case class RequestToJoin(fromId: String, from: ActorRef[Participant.Activity]) extends Action

  final case class RequestToQuit(fromId: String) extends Action

  final case class UpdateRoomName(newName: String) extends Action

  final case class AssignHost(newHostId: String, newHost:ActorRef[Participant.Activity]) extends Action

  final case class RequestToRemove(participantId: String, byId: String) extends Action

  final case class ContentToRoom(content: Content, participantId: String, from: ActorRef[Participant.Activity]) extends Action


}

class Chatroom(context: ActorContext[Chatroom.Action],
               roomId: String,
               roomName: String,
               host: (String, ActorRef[Participant.Activity]),
               lastMessage: Option[Message],
               chatroom:ChatRoomRepository[Future]
             //  messag:
              ) extends AbstractBehavior[Chatroom.Action](context) {
  import Chatroom._
  private val _roomId = roomId
  private var _roomName = roomName
  private var _host = host
  private val participants: mutable.Map[String, ActorRef[Participant.Activity]] =  mutable.Map.apply(host)
  private var _lastMessage: Option[Message] = lastMessage
  context.system.receptionist ! Receptionist.Register(ServiceKey[Action]("ChatroomKey"+roomId), context.self)

  override def onMessage(msg: Action): Behavior[Action] = {
    msg match {
          case UpdateRoomName(newName) =>
            this._roomName = newName
            this
          case AssignHost(id,ref) =>
            this._host = id -> ref
            this
          case RequestToJoin(id, from) =>
            participants.addOne(id->from)
            from ! Participant.Joined(roomId)
            this
          case RequestToQuit(id) =>
             participants.remove(id)
             this
          case RequestToRemove(id, by) =>
             participants.remove(id).foreach(_ ! Participant.ExitedFrom(_roomId))
             this
          case ContentToRoom(content, fromId, from) =>
            participants.filterNot(_._1==fromId).values.foreach(_ ! Participant.Receive(content))
            _lastMessage = Some(Message( UUID.randomUUID(), fromId, content, Instant.now()))
            this
    }
  }
}