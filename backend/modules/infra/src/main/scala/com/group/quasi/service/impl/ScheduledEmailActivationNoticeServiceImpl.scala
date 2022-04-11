package com.group.quasi.service.impl

import akka.actor.typed.ActorSystem
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.typed.scaladsl.ActorSource
import akka.stream.{CompletionStrategy, OverflowStrategy, QueueOfferResult}
import com.group.quasi.domain.infra.notification.NotificationData
import com.group.quasi.notification.sender.SmtpEmailSender
import com.group.quasi.repository.notification.EmailRepository
import com.group.quasi.service.NotificationService
import com.group.quasi.service.NotificationService._
import com.typesafe.akka.extension.quartz.QuartzSchedulerTypedExtension

import scala.concurrent.Future
import scala.util.{Failure, Success}

final class ScheduledEmailActivationNoticeServiceImpl(
    scheduler: QuartzSchedulerTypedExtension,
    emailRepository: EmailRepository,
    emailSender: SmtpEmailSender[Future],
)(implicit actorSystem: ActorSystem[Nothing])
    extends NotificationService[Future]
    with AutoCloseable {

  private val (actor, senderSource) = ActorSource
    .actorRef[NotificationCommand](
      completionMatcher = { case NotificationComplete => CompletionStrategy.immediately },
      failureMatcher = { case NotificationFailure(ex) => ex },
      bufferSize = 512,
      overflowStrategy = OverflowStrategy.fail,
    )
    .preMaterialize()

  senderSource
    .collectType[SendBatch.type]
    .flatMapConcat(_ => Source.future(emailRepository.findLatestBatch()))
    .flatMapConcat(Source(_))
    .mapAsync(1)(emailSender.send)
    .mapAsync(1)(emailRepository.removeSent)
    //.recoverWithRetries()
    .runWith(Sink.onComplete {
      case Success(value) => ()
      case Failure(e)     => ()
    })

  private val insertQueue =
    Source
      .queue[NotificationData](512)
      .mapAsync(1)(emailRepository.insert)
      //.recoverWithRetries()
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
