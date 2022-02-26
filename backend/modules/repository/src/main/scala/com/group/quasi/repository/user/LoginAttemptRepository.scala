package com.group.quasi.repository.user

import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.group.quasi.domain.model.users.ActivationKey
import com.group.quasi.domain.persistence.operation
import com.group.quasi.repository.{PostgresProfile, SlickRepository}

import scala.concurrent.Future

class LoginAttemptRepository(implicit override val session: SlickSession)
    extends SlickRepository
    with operation.LoginAttemptRepository[Future] {

  import PostgresProfile.api._
  private val loginAttempts = TableQuery[LoginAttempts]
  class LoginAttempts(tag: Tag) extends Table[(String, Int)](tag, "login_attempts") {
    def requestFrom = column[String]("request_from")
    def count = column[Int]("count")
    def * = (requestFrom, count)
  }
  def updateCount(requestFrom: String): Future[Option[Int]] = {
    val action =
      sqlu"insert into login_attempts (request_from, count) values ( ${requestFrom},1) on conflict(request_from) do update set count = login_attempts.count + 1" andThen
        loginAttempts.filter(_.requestFrom===requestFrom).map(_.count).result.headOption
    session.db.run(action.transactionally)
  }

  def resetCount(requestFrom: String): Future[Int] = {
    session.db.run(loginAttempts.filter(_.requestFrom===requestFrom).delete)
  }


}


