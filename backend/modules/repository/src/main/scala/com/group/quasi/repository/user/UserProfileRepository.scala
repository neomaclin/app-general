package com.group.quasi.repository.user

import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.group.quasi.domain.model.users.UserProfile
import com.group.quasi.repository.{PostgresProfile, SlickRepository}

import java.time.Instant
import scala.concurrent.Future

class UserProfileRepository(implicit override val session: SlickSession) extends SlickRepository {

  import PostgresProfile.api._

  def insert(profile: UserProfile): Future[Int] = {
    session.db.run(TableQuery[UserProfiles] += profile)
  }

  def update(profile: UserProfile): Future[Int] = {
    session.db.run(TableQuery[UserProfiles] update profile)
  }
  class UserProfiles(tag: Tag) extends Table[UserProfile](tag, "user_profiles") {
    def id = column[Long]("id", O.PrimaryKey)
    def userId = column[Long]("user_id")
    def lastName = column[String]("last_name")
    def firstName = column[String]("first_name")
    def alsoKnowAs = column[String]("aka_name")
    def preferredContact = column[String]("preferred_contact")
    def gender = column[String]("gender")
    def snAccounts = column[List[String]]("social_network_accounts")
    def updatedOn = column[Instant]("updated_on")
    def memo = column[String]("memo")
    def * = (
      id,
      userId,
      lastName,
      firstName,
      alsoKnowAs,
      preferredContact,
      gender,
      snAccounts,
      updatedOn,
      memo,
    ) <> (UserProfile.tupled, UserProfile.unapply)
  }
}
