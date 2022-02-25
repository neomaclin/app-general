package com.group.quasi.domain.model.chat

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import com.group.quasi.domain.model.contents.Content

object Chatroom {
  sealed trait Action
  final case class RequestToJoin(participantId: String, from: ActorRef[Participant.Activity]) extends Action
  final case class RequestToQuit(participantId: String, from: ActorRef[Participant.Activity]) extends Action
  final case class ContentToRoom(content: Content, participantId:String, from: ActorRef[Participant.Activity]) extends Action

  private def roomOf(roomId: String,
                     participants: Set[ActorRef[Participant.Activity]]
                    ): Behavior[Chatroom.Action] =
      Behaviors.receiveMessage{
        case Chatroom.RequestToJoin(id, from) =>
          from ! Participant.Joined(roomId)
          roomOf(roomId, participants + from)
        case Chatroom.RequestToQuit(id, from) =>
          from ! Participant.ExitedFrom(roomId)
          roomOf(roomId, participants - from)
        case Chatroom.ContentToRoom(content, fromId, from) =>
          
          participants.foreach(_ ! Participant.Receive(content))
          Behaviors.same
      }

  def apply(roomId: String, participants: Set[ActorRef[Participant.Activity]]): Behavior[Chatroom.Action] = Behaviors.setup{
    context => roomOf(roomId,participants)
  }

}
