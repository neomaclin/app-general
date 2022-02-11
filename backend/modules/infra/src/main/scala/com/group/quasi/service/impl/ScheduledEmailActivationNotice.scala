package com.group.quasi.service.impl

import akka.actor.typed.ActorSystem
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.typed.scaladsl.ActorSource
import akka.stream.{OverflowStrategy, QueueOfferResult}
import com.group.quasi.domain.infra.notification.NotificationData
import com.group.quasi.domain.service.NotificationService
import com.group.quasi.domain.service.NotificationService._
import com.group.quasi.notification.sender.SmtpEmailSender
import com.group.quasi.repository.notification.EmailRepository
import com.typesafe.akka.extension.quartz.QuartzSchedulerTypedExtension

import scala.concurrent.Future
import scala.util.{Failure, Success}

final class ScheduledEmailActivationNotice(
    scheduler: QuartzSchedulerTypedExtension,
    emailRepository: EmailRepository,
    emailSender: SmtpEmailSender[Future],
)(implicit actorSystem: ActorSystem[Nothing])
    extends NotificationService[Future]
    with AutoCloseable {

  private val (actor, senderSource) = ActorSource
    .actorRef[NotificationCommand](
      completionMatcher = { case NotificationComplete => },
      failureMatcher = { case NotificationFailure(ex) => ex },
      bufferSize = 512,
      overflowStrategy = OverflowStrategy.fail,
    )
    .preMaterialize()

  senderSource
    .collectType[SendBatch.type]
    .mapAsync(2)(_ => emailRepository.findLatestBatch())
    .flatMapConcat(Source(_))
    .mapAsync(2)(emailSender.send)
    .sliding(512)
    .mapAsync(2)(emailRepository.removeSent)
    .runWith(Sink.onComplete {
      case Success(value) => ()
      case Failure(e)     => ()
    })

  private val insertQueue =
    Source
      .queue[NotificationData](512)
      .mapAsync(4)(emailRepository.insert)
      .to(Sink.ignore)
      .run()

  scheduler.scheduleTyped("Every5Seconds", actor, SendBatch)

  def send(data: NotificationData): Future[NotificationResult] = {
    val result = insertQueue.offer(data) match {
      case QueueOfferResult.Failure(e)  => SystemError(e)
      case QueueOfferResult.QueueClosed => SystemOverloaded
      case QueueOfferResult.Dropped     => SystemOverloaded
      case QueueOfferResult.Enqueued    => NotificationEnqueued
    }

    Future.successful(result)
  }

  def close(): Unit = {
    actor ! NotificationComplete
    actorSystem.classicSystem.registerOnTermination(insertQueue.complete())
  }
}
