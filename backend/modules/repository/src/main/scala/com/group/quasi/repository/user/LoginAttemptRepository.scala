package com.group.quasi.repository.user

import akka.actor.ActorSystem
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{Sink, Source}
import com.group.quasi.domain.persistence.repository.DBConfig
import com.group.quasi.repository.{PostgresProfile, SlickRepository}
import com.group.quasi.domain.persistence.operation
import java.util.UUID
import scala.concurrent.Future

class LoginAttemptRepository(override val config: DBConfig, val postgresProfile: PostgresProfile)(implicit
    override val system: ActorSystem,
) extends SlickRepository
    with operation.LoginAttemptRepository[Future] {

  import postgresProfile.api._
  import LoginAttemptRepository._
  class LoginAttempts(tag: Tag) extends Table[LoginAttempt](tag, "login_attempts") {
    def id = column[UUID]("id", O.PrimaryKey)
    def requestFrom = column[String]("request_from")
    def count = column[Int]("count")
    def * = (id, requestFrom, count) <> (LoginAttempt.tupled, LoginAttempt.unapply)
  }

  def updateCount(requestFrom: String): Future[Int] = {
    Slick
      .source(TableQuery[LoginAttempts].filter(_.requestFrom === requestFrom).result)
      .fold(LoginAttempt(UUID.randomUUID(),requestFrom,0))((acc, item)=> acc.copy(id=item.id,count = item.count+1))
      .via(Slick.flow[LoginAttempt](TableQuery[LoginAttempts] += _))
      .log("update-login-init")
      .runWith(Sink.fold(0)(_ + _))
  }
  def getCount(requestFrom: String): Future[Option[Int]] = {
    Slick
      .source(TableQuery[LoginAttempts].filter(_.requestFrom === requestFrom).map(_.count).result)
      .log("check-login-count")
      .runWith(Sink.headOption)
  }

}

object LoginAttemptRepository {
  final case class LoginAttempt(id: UUID, requestFrom: String, count: Int)
}
