package com.group.quasi.service

import com.group.quasi.domain.service.{BootstrapService, UserService}
import com.group.quasi.service.impl.{BootstrapServiceImpl, ScheduledEmailActivationNotice, UserServiceImpl}
import com.typesafe.akka.extension.quartz.QuartzSchedulerTypedExtension
import distage.{ModuleDef, TagK}

class ServiceModule[F[+_]: TagK] extends ModuleDef {
  make[UserService[F]].from[UserServiceImpl[F]]
  make[ScheduledEmailActivationNotice]
  make[BootstrapService[F]].from[BootstrapServiceImpl[F]]
  make[QuartzSchedulerTypedExtension]
}
