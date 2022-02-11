package com.group.quasi.repository.notification

import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.group.quasi.domain.infra.notification.NotificationData
import com.group.quasi.repository.{PostgresProfile, SlickRepository}

import scala.concurrent.Future

class SMSRepository(implicit override val session: SlickSession) extends SlickRepository {
  import PostgresProfile.api._
  import slick.jdbc.GetResult
  implicit private val getResult: GetResult[NotificationData] =
    GetResult(r => NotificationData(r.nextString(), r.nextString(), r.nextString()))

  def insert(entry: NotificationData): Future[Int] = {
    session.db.run(
      sqlu"""INSERT INTO scheduled_sms (recipient,subject,content) VALUES(${entry.recipient}, ${entry.subject} ,${entry.content})""",
    )
  }

  def findLatestBatch(): Future[Seq[NotificationData]] = {
    session.db.run(
      sql"""select recipient,subject,content from scheduled_sms order by created_on desc limit 1024"""
        .as[NotificationData],
    )
  }

  def removeSent(entries: Seq[NotificationData]): Future[Int] = {
    session.db.run(
      sqlu"""delete from scheduled_sms where recipient in ${entries.map(_.recipient).mkString("(", ",", ")")}""",
    )
  }

}
