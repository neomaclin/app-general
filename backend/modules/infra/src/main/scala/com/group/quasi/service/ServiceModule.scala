package com.group.quasi.service

import akka.actor.typed.{ActorSystem, SpawnProtocol}
import cats.MonadThrow
import com.group.quasi.domain.config.notification.{NotificationConfigs, SmtpConfig}
import com.group.quasi.domain.service.UserService
import com.group.quasi.notification.sender.SmtpEmailSender
import com.group.quasi.repository.RepoMigration
import com.group.quasi.repository.notification.EmailRepository
import com.group.quasi.runtime.akka.FutureClock
import com.group.quasi.service.impl.{BootstrapServiceImpl, ScheduledEmailActivationNoticeServiceImpl, UserServiceImpl}
import com.typesafe.akka.extension.quartz.QuartzSchedulerTypedExtension
import distage.{ModuleDef, TagK}
import izumi.functional.mono.Clock

import scala.concurrent.Future

class ServiceModule[F[+_]: TagK] extends ModuleDef {
  make[UserService[F]].from[UserServiceImpl[F]]
  make[NotificationService[F]].from {
    (
        scheduler: QuartzSchedulerTypedExtension,
        emailRepository: EmailRepository,
        config: NotificationConfigs,
        ev: MonadThrow[F],
        system: ActorSystem[SpawnProtocol.Command],
    ) =>
      val service = if (TagK[Future].hasPreciseClass) {
        val emailSender = config.configs
          .get(config.currentOption)
          .collect { case smtp: SmtpConfig =>
            new SmtpEmailSender(smtp)(ev)
          }
          .get
        new ScheduledEmailActivationNoticeServiceImpl(
          scheduler,
          emailRepository,
          emailSender.asInstanceOf[SmtpEmailSender[Future]],
        )(system)
      } else {
        throw new RuntimeException("only support future")
      }
      system.classicSystem.registerOnTermination(service.close())
      service.asInstanceOf[NotificationService[F]]
  }
  make[ScheduledEmailActivationNoticeServiceImpl]
  make[BootstrapService[F]].from { (migration: RepoMigration) =>
    val service = if (TagK[Future].hasPreciseClass) {
      new BootstrapServiceImpl(migration)
    } else {
      throw new RuntimeException("only support future")
    }
    service.asInstanceOf[BootstrapService[F]]
  }
  make[QuartzSchedulerTypedExtension].from((system: ActorSystem[SpawnProtocol.Command]) =>
    new QuartzSchedulerTypedExtension(system),
  )
  make[Clock[F]].from[FutureClock[F]]

}
