package com.group.quasi.domain.infra.notification

trait NotificationSender[F[_]] {
  def send(data: NotificationData): F[NotificationData]
}
