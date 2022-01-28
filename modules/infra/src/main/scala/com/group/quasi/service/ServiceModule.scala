package com.group.quasi.service

import com.group.quasi.domain.service.UserService
import distage.{ModuleDef, TagK}

class ServiceModule[F[+_]:TagK] extends ModuleDef{
  make[UserService[F]].from[UserServiceImpl[F]]
}