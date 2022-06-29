package com.group.quasi.notification.sender

import cats.MonadThrow
import cats.implicits._
import com.group.quasi.domain.config.notification.{NotificationData, NotificationSender, SmtpConfig}

import java.util.{Date, Properties}
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Address, Message, Session, Transport}
import scala.util.control.NonFatal

/** Sends emails synchronously using SMTP.
  */
class SmtpEmailSender[F[_]: MonadThrow](config: SmtpConfig) extends NotificationSender[F] {

  def send(notification: NotificationData): F[NotificationData] = (for {
    emailToSend <- MonadThrow[F].pure(
      new SmtpEmailSender.EmailDescription(
        List(notification.recipient),
        notification.content,
        notification.subject,
      ),
    )
    _ <- MonadThrow[F].catchNonFatal(
      SmtpEmailSender.send(
        config.host,
        config.port,
        config.username,
        config.password,
        config.verifySslCertificate,
        config.sslConnection,
        config.from,
        config.encoding,
        emailToSend,
      ),
    ).recover{
      case NonFatal(e) => println(e)
    }
  } yield notification).recover(_ => notification)

}

//
/** Copied from softwaremill-common:
  * https://github.com/softwaremill/softwaremill-common/blob/master/softwaremill-sqs/src/main/java/com/softwaremill/common/sqs/email/EmailSender.java
  */
object SmtpEmailSender {

  def send(
      smtpHost: String,
      smtpPort: Int,
      smtpUsername: String,
      smtpPassword: String,
      verifySSLCertificate: Boolean,
      sslConnection: Boolean,
      from: String,
      encoding: String,
      emailDescription: EmailDescription,
  ): Unit = {

    val props = setupSmtpServerProperties(sslConnection, smtpHost, smtpPort, verifySSLCertificate)

    // Get a mail session
    val session = Session.getInstance(props)

    val m = new MimeMessage(session)
    m.setFrom(new InternetAddress(from))

    val to = convertStringEmailsToAddresses(emailDescription.emails)
    val replyTo = convertStringEmailsToAddresses(emailDescription.replyToEmails)
    val cc = convertStringEmailsToAddresses(emailDescription.ccEmails)
    val bcc = convertStringEmailsToAddresses(emailDescription.bccEmails)

    m.setRecipients(javax.mail.Message.RecipientType.TO, to)
    m.setRecipients(Message.RecipientType.CC, cc)
    m.setRecipients(Message.RecipientType.BCC, bcc)
    m.setReplyTo(replyTo)
    m.setSubject(emailDescription.subject, encoding)
    m.setSentDate(new Date())
    m.setText(emailDescription.message, encoding, "html")

    val transport = createSmtpTransportFrom(session, sslConnection)
    try {
      connectToSmtpServer(transport, smtpUsername, smtpPassword)
      sendEmail(transport, m, emailDescription, to)
    } finally {
      transport.close()
    }
  }

  private def setupSmtpServerProperties(
      sslConnection: Boolean,
      smtpHost: String,
      smtpPort: Int,
      verifySSLCertificate: Boolean,
  ): Properties = {
    // Setup mail server
    val props = new Properties()
    if (sslConnection) {
      props.put("mail.smtps.host", smtpHost)
      props.put("mail.smtps.port", smtpPort.toString)
      props.put("mail.smtps.starttls.enable", "true")
      if (!verifySSLCertificate) {
        props.put("mail.smtps.ssl.checkserveridentity", "false")
        props.put("mail.smtps.ssl.trust", "*")
      }
    } else {
      props.put("mail.smtp.host", smtpHost)
      props.put("mail.smtp.port", smtpPort.toString)
    }
    props
  }

  private def createSmtpTransportFrom(session: Session, sslConnection: Boolean): Transport =
    if (sslConnection) session.getTransport("smtps") else session.getTransport("smtp")

  private def sendEmail(
      transport: Transport,
      m: MimeMessage,
      emailDescription: EmailDescription,
      to: Array[Address],
  ): Unit = {
    transport.sendMessage(m, m.getAllRecipients)
    // logger.debug("Mail '" + emailDescription.subject + "' sent to: " + to.mkString(","))
  }

  private def connectToSmtpServer(transport: Transport, smtpUsername: String, smtpPassword: String): Unit = {
    if (smtpUsername != null && smtpUsername.nonEmpty) {
      transport.connect(smtpUsername, smtpPassword)
    } else {
      transport.connect()
    }
  }

  private def convertStringEmailsToAddresses(emails: Array[String]): Array[Address] =
    emails.map(new InternetAddress(_))

  case class EmailDescription(
      emails: Array[String],
      message: String,
      subject: String,
      replyToEmails: Array[String],
      ccEmails: Array[String],
      bccEmails: Array[String],
  ) {

    def this(emails: List[String], message: String, subject: String) =
      this(emails.toArray, message, subject, Array(), Array(), Array())
  }
}
