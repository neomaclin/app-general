package com.group.quasi.domain.model.chat

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.group.quasi.domain.model.contents.Content

object Participant {
 sealed trait Activity
  final case class ExitedFrom(roomId:String) extends Activity
  final case class Joined(roomId:String) extends Activity
  final case class Receive(content: Content) extends Activity
  def apply(): Behavior[Participant.Activity] = Behaviors.empty
}
