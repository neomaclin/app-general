package com.group.quasi.domain.service

import com.group.quasi.domain.infra.notification.NotificationData
import com.group.quasi.domain.service.NotificationService.NotificationResult

trait NotificationService[F[_]] {
  def send(data: NotificationData): F[NotificationResult]
}

object NotificationService {

  sealed trait NotificationCommand
  case object SendBatch extends NotificationCommand
  case object NotificationComplete extends NotificationCommand
  final case class NotificationFailure(e: Throwable) extends NotificationCommand

  sealed trait NotificationResult
  case object NotificationEnqueued extends NotificationResult
  sealed trait NotificationError extends RuntimeException with NotificationResult
  case object SystemOverloaded extends NotificationError
  final case class SystemError(e: Throwable) extends NotificationError {
    override def getMessage: String = super.getMessage
    override def getCause: Throwable = e
  }
}
