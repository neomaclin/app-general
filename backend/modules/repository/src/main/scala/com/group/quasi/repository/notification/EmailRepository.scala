package com.group.quasi.repository.notification

import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.group.quasi.domain.infra.notification.NotificationData
import com.group.quasi.repository.{PostgresProfile, SlickRepository}

import scala.concurrent.Future

class EmailRepository(implicit override val session: SlickSession) extends SlickRepository {
  import PostgresProfile.api._
  import slick.jdbc.GetResult
  implicit private val getResult: GetResult[NotificationData] =
    GetResult(r => NotificationData(r.nextString(), r.nextString(), r.nextString()))

  class EmailNotification(tag: Tag) extends Table[NotificationData](tag, "scheduled_email") {
    def recipient = column[String]("recipient")
    def subject = column[String]("subject")
    def content = column[String]("content")

    def * = (recipient,subject,content) <> (NotificationData.tupled, NotificationData.unapply)
  }

  private val emailNotifications = TableQuery[EmailNotification]

  def insert(entry: NotificationData): Future[Int] = session.db.run( emailNotifications += entry )

  def findLatestBatch(): Future[Seq[NotificationData]] = {
    session.db.run(
      sql"""select recipient,subject,content from scheduled_email order by created_on desc limit 1024"""
        .as[NotificationData],
    )
  }

  def removeSent(entry: NotificationData): Future[Int] = {
    session.db.run(emailNotifications.filter(_.recipient === entry.recipient).delete)
  }

}
