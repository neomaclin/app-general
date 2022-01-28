package com.group.quasi.repository.resetcode

import akka.actor.ActorSystem
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{Sink, Source}
import com.group.quasi.domain.model.users.User
import com.group.quasi.domain.persistence.repository.DBConfig
import com.group.quasi.repository.{PostgresProfile, SlickRepository}

import java.time.Instant
import scala.concurrent.Future

class PasswordResetCodeRepository(override val config: DBConfig, val postgresProfile: PostgresProfile)(implicit override val system: ActorSystem) extends SlickRepository {

  import postgresProfile.api._

  def insert(code: String, user: User, validUntil: Instant): Future[Int] = {
    Source
      .single(user)
      .via(
        Slick.flow(user => sqlu"""INSERT INTO password_reset_codes (code, user_id, valid_until) VALUES($code, ${user.id} ,${validUntil.toEpochMilli})"""),
      )
      .log("activation_keys-insert")
      .runWith(
        Sink.fold[Int, Int](0)(_ + _),
      )
  }
  def delete(code: String, user: User): Future[Int] = {
    Source
      .single(user)
      .via(
        Slick.flow(user => sqlu"""Delete from password_reset_codes WHERE code = $code user_id = ${user.id}"""),
      )
      .log("activation_keys-delete")
      .runWith(
        Sink.fold[Int, Int](0)(_ + _),
      )
  }

}