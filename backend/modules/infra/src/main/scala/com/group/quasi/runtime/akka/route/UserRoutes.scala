package com.group.quasi.runtime.akka.route

import akka.http.scaladsl.server.Directives.{extractClientIP, extractExecutionContext}
import cats.data.EitherT
import com.group.quasi.app.api.auth.UserProfileEndpoint.UserProfileExt
import com.group.quasi.app.api.auth._
import com.group.quasi.domain.model.users.{SuccessContent, UserConfig}
import com.group.quasi.domain.service.UserService
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import org.slf4j.LoggerFactory
import pdi.jwt.{JwtClaim, JwtHeader}
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.Future
import scala.util.Try

class UserRoutes(
    userService: UserService[Future],
    userConfig: UserConfig,
    jwtConfig: JwtConfig,
    interpreter: AkkaHttpServerInterpreter,
    securedUserEndpoint: UserEndpoint.SecuredUserEndpoint,
) {
  private val logger = LoggerFactory.getLogger(this.getClass)
  def checkClaims(
      jwtToken: Try[(JwtHeader, JwtClaim, String)],
  ): Future[Either[Unit, SuccessContent]] = {
    Future.successful(jwtToken.flatMap(_._2.content.asJson.as[SuccessContent].toTry).toEither.left.map(_ => ()))
  }

  val loginRoute =
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
  val signupRoutes = extractExecutionContext { implicit ec =>
    interpreter.toRoute(
      List(
        UserEndpoint.register.serverLogic { request =>
          EitherT(userService.register(request.login, request.password, request.email, request.phone))
            .bimap(
              e => { logger.debug("", e); RegisterFailure("Failed to register") },
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
            EitherT(userService.updatePassword(content.user, request.current, request.proposed))
              .bimap(_ => (), _ => PasswordResetResponse("update succeed"))
              .value
          },
      ),
    )
  }
  private val profileEndpoint = new UserProfileExt(securedUserEndpoint)
  val profileRoutes = extractExecutionContext { implicit ec =>
    interpreter.toRoute(
      List(
        profileEndpoint.get
          .serverSecurityLogic { checkClaims }
          .serverLogic { content => request =>
            EitherT(userService.getUserProfile(content.user))
              .bimap(
                _ => (),
                model =>
                  UserProfile(
                    lastName = model.lastName,
                    firstName = model.firstName,
                    aka = model.alsoKnowAs,
                    preferredContact = model.preferredContact,
                    gender = model.preferredContact,
                    snAccounts = model.snAccounts,
                    memo = model.memo,
                  ),
              )
              .value
          },
        profileEndpoint.create
          .serverSecurityLogic { checkClaims }
          .serverLogic { content => request =>
            EitherT(
              userService.createOrUpdateUserProfile(
                user = content.user,
                lastName = request.lastName,
                firstName = request.firstName,
                alsoKnowAs = request.aka,
                preferredContact = request.preferredContact,
                gender = request.gender,
                snAccounts = request.snAccounts,
                memo = request.memo,
              ),
            )
              .bimap(_ => (), _ => ())
              .value
          },
        profileEndpoint.edit
          .serverSecurityLogic { checkClaims }
          .serverLogic { content => request =>
            EitherT(
              userService.createOrUpdateUserProfile(
                user = content.user,
                lastName = request.lastName,
                firstName = request.firstName,
                alsoKnowAs = request.aka,
                preferredContact = request.preferredContact,
                gender = request.gender,
                snAccounts = request.snAccounts,
                memo = request.memo,
              ),
            )
              .bimap(_ => (), _ => ())
              .value
          },
      ),
    )
  }
}
