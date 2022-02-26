package com.group.quasi.runtime.akka

import akka.actor.ActorSystem
import akka.actor.typed.SpawnProtocol
import akka.actor.typed.scaladsl.Behaviors
import com.group.quasi.domain.infra.HttpConfig
import com.group.quasi.domain.service.{BootstrapService, UserService}
import com.group.quasi.service.impl.UserServiceImpl
import distage.{Lifecycle, ModuleDef, TagK}
import exchange.api.auth.UserEndpoint

import scala.concurrent.Future

class AkkaRuntimeModule extends ModuleDef {
  make[akka.actor.typed.ActorSystem[SpawnProtocol.Command]].fromResource(
    Lifecycle.makeSimple(akka.actor.typed.ActorSystem[SpawnProtocol.Command](GuardianBehavior(), "app"))(_.terminate()),
  )
  make[ActorSystem].from((_: akka.actor.typed.ActorSystem[SpawnProtocol.Command]).classicSystem)
  make[AkkaApiRouteProvider]
  make[UserEndpoint.SecuredUserEndpoint]

}
