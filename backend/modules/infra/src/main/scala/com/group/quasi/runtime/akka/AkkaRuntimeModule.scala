package com.group.quasi.runtime.akka

import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import com.group.quasi.domain.service.UserService
import com.group.quasi.service.impl.UserServiceImpl
import distage.{Lifecycle, ModuleDef, TagK}
import exchange.api.auth.UserEndpoint

class AkkaRuntimeModule[F[_]: TagK] extends ModuleDef {
  make[akka.actor.typed.ActorSystem[Nothing]].fromResource(
    Lifecycle.makeSimple(akka.actor.typed.ActorSystem[Nothing](Behaviors.empty, "app"))(_.terminate()),
  )
  make[ActorSystem].from((_: akka.actor.typed.ActorSystem[Nothing]).classicSystem)
  make[AkkaApiRouteProvider]
  make[UserEndpoint.SecuredUserEndpoint]
  make[UserService[F]].from[UserServiceImpl[F]]

}
