package com.group.quasi.runtime.akka

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import cats.MonadThrow
import com.group.quasi.MainModule
import com.group.quasi.domain.infra.HttpConfig
import com.group.quasi.domain.service.BootstrapService
import distage._

import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

trait AkkaHttpServer {

  import cats.implicits._

  private val module = new MainModule[Future] ++ new ModuleDef {

    make[MonadThrow[Future]].from((system: ActorSystem[Nothing]) =>
      catsStdInstancesForFuture(system.classicSystem.dispatcher),
    )

  }

  def run: ServerBinding = {
    Injector().produceRun(module, Activation(Mode -> Mode.Prod)) {
      (
          routeProvider: AkkaApiRouteProvider,
          akkaSystem: ActorSystem[Nothing],
          httpConfig: HttpConfig,
          bootstrap: BootstrapService[Future],
      ) =>
        implicit val system = akkaSystem.classicSystem
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
