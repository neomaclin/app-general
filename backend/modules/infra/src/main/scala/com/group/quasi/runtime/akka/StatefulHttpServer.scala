package com.group.quasi.runtime.akka


import akka.actor
import akka.actor.typed.{ActorSystem, SpawnProtocol}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import com.group.quasi.MainModule
import com.group.quasi.domain.config.HttpConfig
import com.group.quasi.runtime.akka.route.AkkaApiRouteProvider
import com.group.quasi.service.BootstrapService
import distage._

import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

trait StatefulHttpServer {

  def run: ServerBinding = {
    Injector().produceRun(new MainModule[Future] , Activation(Mode -> Mode.Prod)) {
      (
          routeProvider: AkkaApiRouteProvider,
          akkaSystem: ActorSystem[SpawnProtocol.Command],
          httpConfig: HttpConfig,
          bootstrap: BootstrapService[Future],
      ) =>
        implicit val system: actor.ActorSystem = akkaSystem.classicSystem
        implicit val executionContext: ExecutionContextExecutor = system.dispatcher
        Await.result(
          for {
            server <- Http().newServerAt(httpConfig.host, httpConfig.port).bind(Route.seal(routeProvider.allRoutes))
            _ <- bootstrap.initialize()
            _ <- Future.never
          } yield server.addToCoordinatedShutdown(hardTerminationDeadline = 10.seconds),
          Duration.Inf,
        )
    }
  }


}
