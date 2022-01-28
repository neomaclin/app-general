package com.group.quasi.domain.service

import com.group.quasi.domain.infra.notification.NotificationData

trait NotificationService[F[_]] {
  def send(data: NotificationData): F[Unit]
}
