package com.group.quasi.repository

import akka.stream.alpakka.slick.scaladsl.SlickSession

abstract class SlickRepository {
  implicit def session: SlickSession
}
