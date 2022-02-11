package com.group.quasi.service

import com.group.quasi.domain.service.UserService
import com.group.quasi.service.impl.{ScheduledEmailActivationNotice, UserServiceImpl}
import com.typesafe.akka.extension.quartz.QuartzSchedulerTypedExtension
import distage.{ModuleDef, TagK}

class ServiceModule[F[+_]: TagK] extends ModuleDef {
  make[UserService[F]].from[UserServiceImpl[F]]
  make[ScheduledEmailActivationNotice]
  make[QuartzSchedulerTypedExtension]
}
