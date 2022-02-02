package com.group.quasi.domain.infra.notification

import cats.Applicative

class NoopNotificationSender[F[_]: Applicative] extends NotificationSender[F] {
  override def send(data: NotificationData): F[Unit] = Applicative[F].unit
}
