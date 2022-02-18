package com.group.quasi

import cats.MonadThrow
import cats.implicits._
import izumi.functional.mono.{Clock, ClockAccuracy}
import izumi.fundamentals.platform.time.IzTime.TZ_UTC

import java.time.{LocalDateTime, OffsetDateTime, ZonedDateTime}

class FutureClock[F[_]:MonadThrow]() extends Clock[F]{

  override def epoch: F[Long] = java.time.Clock.systemUTC().millis().pure

  override def now(accuracy: ClockAccuracy): F[ZonedDateTime] = {
    ClockAccuracy.applyAccuracy(ZonedDateTime.now(TZ_UTC), accuracy).pure
  }

  override def nowLocal(accuracy: ClockAccuracy): F[LocalDateTime] = {
    now(accuracy).map(_.toLocalDateTime)
  }

  override def nowOffset(accuracy: ClockAccuracy): F[OffsetDateTime] = {
    now(accuracy).map(_.toOffsetDateTime)
  }
}
