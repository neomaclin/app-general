package com.group.quasi.repository.notification

import akka.actor.ActorSystem
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{Sink, Source}
import com.group.quasi.domain.infra.notification.NotificationData
import com.group.quasi.domain.persistence.repository.DBConfig
import com.group.quasi.repository.{PostgresProfile, SlickRepository}

import scala.concurrent.Future


class EmailRepository(override val config: DBConfig, val postgresProfile: PostgresProfile)(implicit override val system: ActorSystem) extends SlickRepository {
  import postgresProfile.api._
  import slick.jdbc.GetResult
  implicit private val getResult: GetResult[NotificationData] = GetResult(r => NotificationData(r.nextString(), r.nextString(), r.nextString()))

  def insert(recipient: String, subject: String, content: String): Future[Int] = {
    Source
      .single(recipient)
      .via(Slick.flow(recipient => sqlu"""INSERT INTO scheduled_email (recipient,subject,content) VALUES($recipient, $subject ,$content)"""))
      .log("email.content-insert")
      .runWith(Sink.fold[Int, Int](0)(_ + _))
  }

  def findLatestTen(): Future[Seq[NotificationData]] = {
    Slick
      .source(sql"""select recipient,subject,content from scheduled_email order by created_on desc limit 10""".as[NotificationData])
      .runWith(Sink.seq)
  }

}

