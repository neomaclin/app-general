package com.group.quasi.repository.user

import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.group.quasi.domain.persistence.operation
import com.group.quasi.repository.{PostgresProfile, SlickRepository}

import scala.concurrent.Future

class LoginAttemptRepository(implicit override val session: SlickSession)
    extends SlickRepository
    with operation.LoginAttemptRepository[Future] {

  import PostgresProfile.api._

  def updateCount(requestFrom: String): Future[Option[Int]] = {
    val action =
      sqlu"""insert into login_attempts (request_from) values ( $requestFrom )
            | on conflict(request_from) do update set count = login_attempts.count + 1""" andThen
      sql"select count from login_attempts where request_from = $requestFrom".as[Int].headOption
    session.db.run(action.transactionally)
  }

  def resetCount(requestFrom: String): Future[Int] = {
    val action = sqlu"""delete from login_attempts where request_from = $requestFrom"""
    session.db.run(action.transactionally)
  }


}


