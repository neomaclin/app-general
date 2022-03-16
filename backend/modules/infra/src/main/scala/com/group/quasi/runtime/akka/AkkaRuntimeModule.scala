package com.group.quasi.runtime.akka

import akka.actor.typed.{ActorSystem, SpawnProtocol}
import cats.MonadThrow
import cats.implicits.catsStdInstancesForFuture
import com.group.quasi.runtime.akka.route.{AkkaApiRouteProvider, RoutesModule}
import distage.{Lifecycle, ModuleDef}

import scala.concurrent.Future

class AkkaRuntimeModule extends ModuleDef {
  include(new RoutesModule)
  make[MonadThrow[Future]].from((system: ActorSystem[SpawnProtocol.Command]) =>
    catsStdInstancesForFuture(system.classicSystem.dispatcher),
  )
  make[akka.actor.typed.ActorSystem[SpawnProtocol.Command]].fromResource(
    Lifecycle.makeSimple(akka.actor.typed.ActorSystem[SpawnProtocol.Command](GuardianBehavior(), "app"))(_.terminate()),
  )
  make[akka.actor.ActorSystem].from((_: akka.actor.typed.ActorSystem[SpawnProtocol.Command]).classicSystem)
  make[AkkaApiRouteProvider]
}
