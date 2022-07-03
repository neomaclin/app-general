package com.group.quasi.domain.model.chat

import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext}
import akka.actor.typed.{ActorRef, Behavior}
import com.group.quasi.domain.model.contents.Content
import com.group.quasi.domain.model.contents.messages.Message
import com.group.quasi.domain.model.users.UserId
import com.group.quasi.domain.persistence.operation.{ChatRoomRepository, MessageRepository}

import java.time.Instant
import java.util.UUID
import scala.collection.mutable
import scala.concurrent.Future


object Chatroom {

  sealed trait Action

  final case class RequestToJoin(fromId: UserId, from: ActorRef[Participant.Activity]) extends Action

  final case class RequestToQuit(fromId: UserId) extends Action

  final case class UpdateRoomName(newName: String) extends Action

  sealed trait RepoResponseResult
  private final case class WrappedUpdateResult(result: RepoResponseResult, replyTo: ActorRef[Participant.Activity]) extends Action

  final case class AssignHost(newHostId: UserId, newHost:ActorRef[Participant.Activity]) extends Action

  final case class RequestToRemove(participantId: UserId, byId: UserId) extends Action

  final case class ContentToRoom(content: Content, participantId: UserId, from: ActorRef[Participant.Activity]) extends Action


}

class Chatroom(context: ActorContext[Chatroom.Action],
               roomId: String,
               roomName: String,
               host: (UserId, ActorRef[Participant.Activity]),
               lastMessage: Option[Message],
               chatroomRepository :ChatRoomRepository[Future],
               messageRepository: MessageRepository[Future]
              ) extends AbstractBehavior[Chatroom.Action](context) {
  import Chatroom._
  private val _roomId = roomId
  private var _roomName = roomName
  private var _host = host
  private val participants: mutable.Map[UserId, ActorRef[Participant.Activity]] =  mutable.Map.apply(host)
  private var _lastMessage: Option[Message] = lastMessage
  context.system.receptionist ! Receptionist.Register(ServiceKey[Action]("ChatroomKey"+roomId), context.self)

  override def onMessage(msg: Action): Behavior[Action] = {
    msg match {
          case WrappedUpdateResult(result, replyTo) =>
            this
          case UpdateRoomName(newName) =>
//            context.pipeToSelf(chatroomRepository.updateRoomName(this._roomName, newName, host._1)){
//              case Success(_) =>
//
//            }
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
//            context.pipeToSelf(messageRepository.persist(_roomId, fromId, _lastMessage.get)){
//              case Success(_) => WrappedUpdateResult(UpdateSuccess(value.id), replyTo)
//              case Failure(e) => WrappedUpdateResult(UpdateFailure(value.id, e.getMessage), replyTo)
//            }
            this
    }
  }
}