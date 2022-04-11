package com.group.quasi.repository.user

import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.group.quasi.domain.model.users.UserProfile
import com.group.quasi.domain.persistence.operation
import com.group.quasi.repository.{PostgresProfile, SlickRepository}

import java.time.Instant
import scala.concurrent.Future

class UserProfileRepository(implicit override val session: SlickSession)
  extends SlickRepository
    with operation.UserProfileRepository[Future] {


  import PostgresProfile.api._

  def insertOrUpdate(profile: UserProfile): Future[Int] = {
    session.db.run(TableQuery[UserProfiles] insertOrUpdate profile)
  }

  class UserProfiles(tag: Tag) extends Table[UserProfile](tag, "user_profiles") {
    def userId = column[Long]("user_id")
    def lastName = column[String]("last_name")
    def firstName = column[String]("first_name")
    def alsoKnowAs = column[String]("aka_name")
    def preferredContact = column[String]("preferred_contact")
    def gender = column[String]("gender")
    def snAccounts = column[List[String]]("social_network_accounts")
    def updatedOn = column[Long]("node_modification_epoch")
    def memo = column[String]("memo")
    def * = (
      userId,
      lastName,
      firstName,
      alsoKnowAs,
      preferredContact,
      gender,
      snAccounts,
      updatedOn.?,
      memo,
    ) <> (UserProfile.tupled, UserProfile.unapply)
  }

  override def lookupBy(userId: Long): Future[Option[UserProfile]] = {
    session.db.run(TableQuery[UserProfiles].filter(_.userId===userId).result.headOption)
  }
}
