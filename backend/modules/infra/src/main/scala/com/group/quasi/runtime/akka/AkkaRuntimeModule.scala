package com.group.quasi.runtime.akka

import akka.actor.ActorSystem
import akka.actor.typed.SpawnProtocol
import com.group.quasi.app.api.auth.UserEndpoint
import distage.{Lifecycle, ModuleDef}

class AkkaRuntimeModule extends ModuleDef {
  make[akka.actor.typed.ActorSystem[SpawnProtocol.Command]].fromResource(
    Lifecycle.makeSimple(akka.actor.typed.ActorSystem[SpawnProtocol.Command](GuardianBehavior(), "app"))(_.terminate()),
  )
  make[ActorSystem].from((_: akka.actor.typed.ActorSystem[SpawnProtocol.Command]).classicSystem)
  make[AkkaApiRouteProvider]
  make[UserEndpoint.SecuredUserEndpoint]

}
