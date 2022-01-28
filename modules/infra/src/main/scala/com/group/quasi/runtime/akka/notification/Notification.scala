package com.group.quasi.runtime.akka.notification

import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors
import akka.stream.scaladsl.{Sink, Source}
import com.group.quasi.notification.email.sender.SmtpEmailSender
import com.group.quasi.repository.notification.EmailRepository

import scala.concurrent.Future

object Notification {
    sealed trait Command
   val NotificationServiceKey = ServiceKey[Command]("pingService")

  case object SendBatch extends Command
    def apply(  emailRepository: EmailRepository,
                emailSender: SmtpEmailSender[Future]
             ): Behavior[Command] =
      Behaviors.setup { implicit ctx =>
        implicit val mat = ctx.system
        implicit val ec = ctx.executionContext
        ctx.system.receptionist ! Receptionist.Register(NotificationServiceKey, ctx.self)

        Behaviors.receiveMessagePartial { case SendBatch =>
          for {
            _ <- Source
              .future(emailRepository.findLatestTen())
              .flatMapConcat(Source(_))
              .mapAsync(2)(emailSender.send)
              .runWith(Sink.ignore)
            _ <- Future.unit
          } yield ()
          Behaviors.same

        }
      }
  }
