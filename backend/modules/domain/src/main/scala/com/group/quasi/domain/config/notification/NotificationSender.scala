package com.group.quasi.domain.config.notification

trait NotificationSender[F[_]] {
  def send(data: NotificationData): F[NotificationData]
}
