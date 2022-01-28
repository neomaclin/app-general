package com.group.quasi.service

import com.group.quasi.domain.infra.notification.NotificationData
import com.group.quasi.domain.service.NotificationService
import com.group.quasi.notification.email.sender.SmtpEmailSender
import com.group.quasi.repository.notification.EmailRepository
import com.typesafe.akka.extension.quartz.QuartzSchedulerTypedExtension

import scala.concurrent.Future

class ScheduledNotificationService(
    scheduler: QuartzSchedulerTypedExtension,
   // actorSystem: ActorSystem[Nothing],
    emailRepository: EmailRepository,
    emailSender: SmtpEmailSender[Future],
) extends NotificationService[Future] {
//  scheduler.scheduleTyped("",actorSystem.,SendBatch)

  def send(data: NotificationData): Future[Unit] = {
    Future.unit
  }

}
