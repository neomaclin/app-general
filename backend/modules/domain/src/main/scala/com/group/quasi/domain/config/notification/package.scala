package com.group.quasi.domain.config

import scala.concurrent.duration.FiniteDuration

package object notification {

  sealed trait NotificationOption

  case object SMTP extends NotificationOption
  case object AwsSNS extends NotificationOption

  object NotificationOption {
    def unsafe(value: String): NotificationOption = value.toLowerCase match {
      case "smtp" => SMTP
      case "awssns" => AwsSNS
      case _ => throw new IllegalArgumentException(s"$value is not supported NotificationOption type")
    }
  }

  sealed trait NotificationConfig

  final case class NotificationConfigs(
      currentOption: NotificationOption,
      configs: Map[NotificationOption, NotificationConfig],
      batchSize: Int,
      SendInterval: FiniteDuration,
  )

  final case class SmtpConfig(
      host: String,
      port: Int,
      username: String,
      password: String,
      sslConnection: Boolean,
      verifySslCertificate: Boolean,
      from: String,
      encoding: String,
  ) extends NotificationConfig

  final case class AwsSNSConfig(
      host: String,
      port: Int,
      username: String,
      password: String,
      sslConnection: Boolean,
      verifySslCertificate: Boolean,
      from: String,
      encoding: String,
  ) extends NotificationConfig

  final case class NotificationData(recipient: String, subject: String, content: String)

  final case class SubjectContent(subject: String, content: String)

  object NotificationDataBuilder {
    def from(recipient: String, subjectContent: SubjectContent): NotificationData = {
      NotificationData(recipient, subjectContent.subject, subjectContent.content)
    }
  }
}
