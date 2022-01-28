package com.group.quasi.repository.user

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.FlowShape
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, Sink, Source}
import com.group.quasi.domain.model.users.UserProfile
import com.group.quasi.domain.persistence.repository.DBConfig
import com.group.quasi.repository.{PostgresProfile, SlickRepository}

import java.time.Instant
import scala.concurrent.Future

class UserProfileRepository(override val config: DBConfig, val postgresProfile: PostgresProfile)(implicit override val system: ActorSystem) extends SlickRepository{

  import postgresProfile.api._

  def insertOrUpdate(profile: UserProfile): Future[Int] = {
    Source.single(profile)
      .via(
        Flow.fromGraph(
          GraphDSL.create() { implicit b =>
            import GraphDSL.Implicits._

            val bcast = b.add(Broadcast[UserProfile](2))
            val merge = b.add(Merge[Int](2))

            val content: Flow[UserProfile,Int, NotUsed] = Slick.flow(TableQuery[UserProfiles].insertOrUpdate _)
            val mark: Flow[UserProfile,Int, NotUsed] = Slick.flow(input => sqlu"""UPDATE user_profiles SET updated_on = "${Instant.now.getEpochSecond}"TRUE WHERE id = ${input.id}""")

            bcast ~> content ~> merge
            bcast ~> mark ~> merge
            FlowShape(bcast.in, merge.out)
          })
      )
      .log("user-profile-insert-or-update")
      .runWith(Sink.fold[Int,Int](0)(_+_))
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
    def memo = column[String]("memo")
    def * = (id, userId, lastName,firstName, alsoKnowAs, preferredContact, gender, snAccounts, memo) <> (UserProfile.tupled, UserProfile.unapply)
  }
}
