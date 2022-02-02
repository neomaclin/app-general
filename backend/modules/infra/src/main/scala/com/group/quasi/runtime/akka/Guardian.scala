package com.group.quasi.runtime.akka

import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.scaladsl.Behaviors
import com.group.quasi.notification.email.sender.SmtpEmailSender
import com.group.quasi.repository.notification.EmailRepository
import com.group.quasi.runtime.akka.notification.Notification
import com.typesafe.akka.extension.quartz.QuartzSchedulerTypedExtension

import scala.concurrent.Future

object Guardian {
  def apply( emailRepository: EmailRepository,
             emailSender: SmtpEmailSender[Future]): Behavior[Nothing] = {
  Behaviors
    .setup[Receptionist.Listing] { context =>
      context.spawnAnonymous(Notification(emailRepository,emailSender))
      val scheduler = QuartzSchedulerTypedExtension(context.system)
      context.system.receptionist ! Receptionist.Subscribe(Notification.NotificationServiceKey, context.self)

      Behaviors.receiveMessagePartial[Receptionist.Listing] {
        case Notification.NotificationServiceKey.Listing(listings) =>
          listings
            .map(notification => scheduler.scheduleTyped("Every5Seconds",notification,Notification.SendBatch))
            .foreach(_ => ())
          Behaviors.same
      }
    }
    .narrow
  }

}
