package com.group.quasi.repository.resetcode

import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.group.quasi.domain.model.users.User
import com.group.quasi.repository.{PostgresProfile, SlickRepository}

import java.time.Instant
import scala.concurrent.Future

class PasswordResetCodeRepository(implicit override val session: SlickSession) extends SlickRepository {

  import PostgresProfile.api._

  def insert(code: String, user: User, validUntil: Instant): Future[Int] = {
    session.db.run(
      sqlu"""INSERT INTO password_reset_codes (code, user_id, valid_until) VALUES($code, ${user.id} ,${validUntil.toEpochMilli})""",
    )
  }

  def delete(code: String, user: User): Future[Int] = {
    session.db.run(sqlu"""Delete from password_reset_codes WHERE code = $code user_id = ${user.id}""")

  }

}
