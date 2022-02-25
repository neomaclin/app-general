package com.group.quasi.runtime.akka

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{Behavior, SpawnProtocol}

object GuardianBehavior {
  def apply(): Behavior[SpawnProtocol.Command] =
    Behaviors.setup { context =>

      SpawnProtocol()
    }
}
