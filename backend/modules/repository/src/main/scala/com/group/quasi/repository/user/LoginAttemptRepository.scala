package com.group.quasi.repository.user

import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.group.quasi.domain.persistence.operation
import com.group.quasi.repository.{PostgresProfile, SlickRepository}

import java.util.UUID
import scala.concurrent.Future

class LoginAttemptRepository(implicit override val session: SlickSession)
    extends SlickRepository
    with operation.LoginAttemptRepository[Future] {

  import LoginAttemptRepository._
  import PostgresProfile.api._

  class LoginAttempts(tag: Tag) extends Table[LoginAttempt](tag, "login_attempts") {
    def id = column[UUID]("id", O.PrimaryKey)
    def requestFrom = column[String]("request_from")
    def count = column[Int]("count")
    def * = (id, requestFrom, count) <> (LoginAttempt.tupled, LoginAttempt.unapply)
  }

  private val loginAttempts = TableQuery[LoginAttempts]
  def updateCount(requestFrom: String): Future[Int] = {
    session.db.run(sqlu"""update login_attempts set count = count + 1 where request_from = $requestFrom""")
  }

  def getCount(requestFrom: String): Future[Option[Int]] = {
    session.db.run(loginAttempts.filter(_.requestFrom === requestFrom).map(_.count).result.headOption)
  }

}

object LoginAttemptRepository {
  final case class LoginAttempt(id: UUID, requestFrom: String, count: Int)
}
