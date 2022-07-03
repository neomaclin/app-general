package com.group.quasi.runtime.akka.route

import akka.http.scaladsl.server.Directives.{extractClientIP, extractExecutionContext}
import cats.implicits.catsSyntaxEitherId
import com.group.quasi.api.heartbeat.{HeartbeatEndpoint, Pong}
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.Future

class HeartbeatRoutes(
    interpreter: AkkaHttpServerInterpreter,
) {
  val ping = extractExecutionContext { implicit ec =>
    interpreter.toRoute(HeartbeatEndpoint.ping.serverLogicPure[Future](_ => Pong().asRight))
  }
}
