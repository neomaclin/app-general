package com.group.quasi.repository.user

import akka.actor.ActorSystem
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{Sink, Source}
import com.group.quasi.domain.model.users.User
import com.group.quasi.domain.persistence.operation
import com.group.quasi.domain.persistence.repository.DBConfig
import com.group.quasi.repository.{PostgresProfile, SlickRepository}

import scala.concurrent.Future

class UserRepository(override val config: DBConfig, val postgresProfile: PostgresProfile)(implicit override val system: ActorSystem)
  extends SlickRepository
    with operation.UserRepository[Future]{

  import postgresProfile.api._

  def insert(user: User): Future[Int] = {
    Source.single(user)
      .via(
        Slick.flow(user => sqlu"""INSERT INTO users (id, alias, email, alias_lowercase, email_lowercase, phone, password, activated)
           | VALUES(${user.id}, ${user.login},${user.email},${user.login},${user.login.toLowerCase},${user.email.toLowerCase}
           | ${user.phone}, ${user.password}, false)""")
      )
      //.log("user")
      .runWith(
        Sink.fold[Int,Int](0)(_+_)
      )
  }

  private def findBy(condition: Users => Rep[Boolean]): Future[Option[User]] = {
    Slick
      .source(TableQuery[Users].filter( condition ).result)
      .log("user")
      .runWith(Sink.headOption)
  }

  def findById(id: Long): Future[Option[User]] = {
    findBy((_: Users).id === id )
  }

  def findByEmail(email: String): Future[Option[User]] = {
    findBy((_: Users).email.toLowerCase === email.toLowerCase)
  }

  def findByLogin(login: String): Future[Option[User]] = {
    findBy((_: Users).login.toLowerCase === login.toLowerCase)
  }

  def findByLoginOrEmail(login: String, email:String): Future[Option[User]] = {
    findBy((user: Users) =>
      List(
        Option(login.toLowerCase).map(user.login.toLowerCase === _),
        Option(email.toLowerCase).map(user.email.toLowerCase === _)
      ).collect({case Some(criteria)  => criteria}).reduceLeftOption(_ || _).getOrElse(true: Rep[Boolean])
    )
  }

  def updatePassword(userId: Long, newPassword: String): Future[Int] =
    Source.single(userId)
      .via(
        // add an optional first argument to specify the parallelism factor (Int)
        Slick.flow(id => sqlu"""UPDATE users SET password = $newPassword WHERE id = $id""")
      )
      .log("update-user-password")
      .runWith(Sink.fold(0)(_+_))

  def updateLogin(userId: Long, newLogin: String): Future[Int] =
    Source.single(userId)
      .via(
        Slick.flow(id => sqlu"""UPDATE users SET alias = $newLogin, alias_lowercase = ${newLogin.toLowerCase} WHERE id = $id""")
      )
      .log("update-user-alias")
      .runWith(Sink.fold(0)(_+_))

  def updateEmail(userId: Long, newEmail: String): Future[Int] =
    Source.single(userId)
      .via(
        Slick.flow(id => sqlu"""UPDATE users SET email_lowercase = $newEmail WHERE id = $id""")
      )
      .log("update-user-email")
      .runWith(Sink.fold(0)(_+_))

  def activate(userId: Long): Future[Int] =
    Source.single(userId)
      .via(
        Slick.flow(id => sqlu"""UPDATE users SET activated = TRUE WHERE id = $id""")
      )
      .log("activate-user")
      .runWith(Sink.fold(0)(_+_))

  class Users(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Long]("id",O.PrimaryKey)
    def login = column[String]("alias")
    def email = column[String]("email")
    def phone = column[String]("phone")
    def password = column[String]("password")
    def * = (id, login, email,password, phone.?) <> (User.tupled, User.unapply)
  }
}
