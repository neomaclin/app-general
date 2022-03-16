package com.group.quasi.runtime.akka.route

import com.group.quasi.api.auth.UserEndpoint
import distage.ModuleDef
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

class RoutesModule  extends ModuleDef {
  make[AkkaHttpServerInterpreter].fromValue(AkkaHttpServerInterpreter())
  make[UserRoutes]
  make[FileRoutes]
  make[ChatRoomRoutes]
  make[UserEndpoint.SecuredUserEndpoint]
}