package com.group.quasi.runtime.akka

import akka.NotUsed
import akka.http.scaladsl.model.HttpEntity.Chunked
import akka.http.scaladsl.model.headers.Authorization
import akka.http.scaladsl.model.{ContentType, HttpResponse, MediaTypes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.alpakka.ftp.scaladsl.Ftp
import akka.stream.alpakka.ftp.{FtpCredentials, FtpSettings}
import akka.stream.alpakka.s3.ObjectMetadata
import akka.stream.alpakka.s3.scaladsl.S3
import akka.stream.scaladsl._
import akka.util.ByteString
import cats.data.EitherT
import cats.implicits._
import com.group.quasi.domain.infra.storage._
import com.group.quasi.domain.model.users.{SuccessContent, UserConfig}
import com.group.quasi.domain.service.UserService
import exchange.api.auth._
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import org.slf4j.LoggerFactory
import pdi.jwt.{JwtCirce, JwtClaim, JwtHeader}
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import java.net.InetAddress
import scala.concurrent.Future
import scala.util.Try

class AkkaApiRouteProvider(
    userService: UserService[Future],
    userConfig: UserConfig,
    jwtConfig: JwtConfig,
    storageConfig: StorageConfigs,
    securedUserEndpoint: UserEndpoint.SecuredUserEndpoint,
) {
  private val logger = LoggerFactory.getLogger(this.getClass)
  private val interpreter: AkkaHttpServerInterpreter = AkkaHttpServerInterpreter()

  private def checkClaims(
      jwtToken: Try[(JwtHeader, JwtClaim, String)],
  ): Future[Either[Unit, SuccessContent]] = {
    Future.successful(jwtToken.flatMap(_._2.content.asJson.as[SuccessContent].toTry).toEither.left.map(_ => ()))
  }

  private val fileUploadRoute = {
    post {
      path("user" / "file" / "upload") {
        headerValueByType(Authorization) { authrization =>
          val token = authrization.credentials.token()
          val jwtToken = JwtCirce.decodeAll(token, jwtConfig.publicKey, Seq(jwtConfig.algo))
          extractRequestContext { ctx =>
            extractExecutionContext { implicit ec =>
              extractMaterializer { implicit mat =>
                fileUpload("file-to-upload") { case (metadata, byteSource) =>
                  val fileName = metadata.fileName

                  val savingFile: Future[Long] = for {
                    userOpt <- checkClaims(jwtToken)
                    result <-
                      if (userOpt.toOption.isDefined) {
                        storageConfig.currentOption match {
                          case AwsS3 =>
                            val bucket = storageConfig.configs(AwsS3).asInstanceOf[S3config].bucket
                            val flow: Flow[ByteString, Option[ObjectMetadata], NotUsed] =
                              Flow.fromSinkAndSourceCoupled(
                                S3.multipartUpload(bucket, fileName),
                                S3.getObjectMetadata(bucket, fileName),
                              )
                            byteSource.via(flow).map(_.map(_.contentLength).getOrElse(0L)).runWith(Sink.fold(0L)(_ + _))
                          case FTP =>
                            val ftpConfig: FtpConfig = storageConfig.configs(FTP).asInstanceOf[FtpConfig]
                            val ftpSettings = FtpSettings
                              .create(InetAddress.getByName(ftpConfig.host))
                              .withPort(ftpConfig.port)
                              .withCredentials(FtpCredentials.create(ftpConfig.userName, ftpConfig.password))
                              .withBinary(true)
                              .withPassiveMode(true)
                            byteSource.runWith(Ftp.toPath(ftpConfig.destination, ftpSettings)).map(_.count)
                          case LocalStorage =>
                            val destination = storageConfig.configs(LocalStorage).asInstanceOf[LocalStorageConfig].path
                            val destinationPath = java.nio.file.Paths.get(destination, fileName)
                            byteSource.runWith(FileIO.toPath(destinationPath).async).map(_.count)
                        }
                      } else Future.successful(0L)
                  } yield result
                  onSuccess(savingFile) { resultCount => complete(s"Uploaded: $resultCount") }
                }
              }
            }
          }

        }

      }
    }
  }

  private val fileDowloadRoute = {
    get {
      path("user" / "file" / "download" / Remaining) { fileName =>
        headerValueByType(Authorization) { authrization =>
          val token = authrization.credentials.token()
          val jwtToken = JwtCirce.decodeAll(token, jwtConfig.publicKey, Seq(jwtConfig.algo))
          extractRequestContext { ctx =>
            extractExecutionContext { implicit ec =>
              extractMaterializer { implicit mat =>
                val file = for {
                  userOpt <- checkClaims(jwtToken)
                  result: HttpResponse <-
                    if (userOpt.toOption.isDefined) {
                      storageConfig.currentOption match {
                        case AwsS3 =>
                          val bucket = storageConfig.configs(AwsS3).asInstanceOf[S3config].bucket
                          for {
                            data <- S3.download(bucket, fileName).runWith(Sink.head) if data.isDefined
                          } yield {
                            HttpResponse(entity =
                              Chunked.fromData(ContentType.Binary(MediaTypes.`application/octet-stream`), data.get._1),
                            )
                          }
                        case FTP =>
                          val ftpConfig: FtpConfig = storageConfig.configs(FTP).asInstanceOf[FtpConfig]
                          val ftpSettings = FtpSettings
                            .create(InetAddress.getByName(ftpConfig.host))
                            .withPort(ftpConfig.port)
                            .withCredentials(FtpCredentials.create(ftpConfig.userName, ftpConfig.password))
                            .withBinary(true)
                            .withPassiveMode(true)
                          Future.successful(
                            HttpResponse(entity =
                              Chunked.fromData(
                                ContentType.Binary(MediaTypes.`application/octet-stream`),
                                Ftp.fromPath(ftpConfig.destination, ftpSettings),
                              ),
                            ),
                          )
                        case LocalStorage =>
                          val destination = storageConfig.configs(LocalStorage).asInstanceOf[LocalStorageConfig].path
                          val destinationPath = java.nio.file.Paths.get(destination, fileName)
                          Future.successful(
                            HttpResponse(entity =
                              Chunked.fromData(
                                ContentType.Binary(MediaTypes.`application/octet-stream`),
                                FileIO.fromPath(destinationPath),
                              ),
                            ),
                          )
                      }
                    } else Future.successful(HttpResponse())
                } yield result
                complete(file)
              }
            }
          }
        }

      }

    }
  }

  def allRoutes: Route = signupRoutes ~ fileUploadRoute ~ fileDowloadRoute ~ loginRoute

  private val loginRoute =
    extractExecutionContext { implicit ec =>
      extractClientIP { address =>
        interpreter.toRoute(
          UserEndpoint.login.serverLogic { request: LoginRequest =>
            EitherT(
              userService.login(
                address.toIP.map(_.ip.toString).getOrElse(""),
                request.login,
                request.password,
                request.email,
                request.phone,
              ),
            )
              .bimap(LoginFailure.from(_, userConfig), LoginResponse.from(_, jwtConfig))
              .value
          },
        )
      }
    }
  private val signupRoutes = extractExecutionContext { implicit ec =>
    interpreter.toRoute(
      List(
        UserEndpoint.register.serverLogic { request =>
          EitherT(userService.register(request.login, request.password, request.email, request.phone))
            .bimap(
              e => {logger.debug("",e); RegisterFailure("Failed to register")},
              RegisterResponse(_, userConfig.activationWindow.toString),
            )
            .value
        },
        UserEndpoint.activate.serverLogic { activationKey =>
          EitherT(userService.activate(activationKey)).bimap(_.msg, _.msg).value
        },
        securedUserEndpoint.changePassword
          .serverSecurityLogic { checkClaims }
          .serverLogic { content => request =>
            EitherT(userService.updatePassword(content.loginAs, request.current, request.proposed))
              .bimap(_ => (), _ => PasswordResetResponse("update succeed"))
              .value
          },
      ),
    )
  }
}
