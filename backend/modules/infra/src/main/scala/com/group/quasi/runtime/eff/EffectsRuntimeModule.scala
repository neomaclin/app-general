package com.group.quasi.runtime.eff

import distage.{DefaultModule, ModuleDef, TagK}
import exchange.api.auth.UserEndpoint
import izumi.distage.model.effect.QuasiIO

class EffectsRuntimeModule[F[_]: TagK: QuasiIO: DefaultModule] extends ModuleDef {
 // make[Http4sApiRouteProvider]
  make[UserEndpoint.SecuredUserEndpoint]
 // make[UserService[F]]
}
