package com.group.quasi.runtime.akka

import com.group.quasi.domain.service.UserService
import com.group.quasi.service.UserServiceImpl
import distage.{ModuleDef, TagK}
import exchange.api.auth.UserEndpoint

class AkkaRuntimeModule[F[_]: TagK] extends ModuleDef {
  make[AkkaApiRouteProvider]
  make[UserEndpoint.SecuredUserEndpoint]
  make[UserService[F]].from[UserServiceImpl[F]]

}
