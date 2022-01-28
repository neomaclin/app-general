package com.group.quasi.repository


import akka.actor.ActorSystem
import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.group.quasi.domain.persistence.repository.DBRepository

abstract class SlickRepository extends DBRepository{
  implicit val session = SlickSession.forConfig(config.profile)
  def system: ActorSystem
  system.registerOnTermination(() => session.close())

}
