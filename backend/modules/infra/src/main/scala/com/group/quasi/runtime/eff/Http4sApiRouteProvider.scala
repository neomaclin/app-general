package com.group.quasi.runtime.eff

import com.group.quasi.domain.model.users.UserConfig
import distage.TagK
import exchange.api.auth.{JwtConfig, UserEndpoint}
import izumi.distage.model.effect.QuasiIO

class Http4sApiRouteProvider[F[_]: TagK: QuasiIO](
    //  userService: UserService[F],
    userConfig: UserConfig,
    jwtConfig: JwtConfig,
    securedUserEndpoint: UserEndpoint.SecuredUserEndpoint,
) {}
