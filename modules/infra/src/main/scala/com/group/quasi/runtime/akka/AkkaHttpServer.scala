package com.group.quasi.runtime.akka

import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import cats.{Applicative, Functor, Monad, MonadThrow}
import com.group.quasi.MainModule
import com.group.quasi.domain.infra.HttpConfig
import distage._

import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

trait AkkaHttpServer {

  import cats.implicits._

  private val module = new MainModule[Future] ++ new ModuleDef {
    make[akka.actor.typed.ActorSystem[Nothing]].fromResource(
      Lifecycle.makeSimple(akka.actor.typed.ActorSystem[Nothing](Behaviors.empty, "app"))(_.terminate()),
    )
    make[ActorSystem].from((_:akka.actor.typed.ActorSystem[Nothing]).classicSystem)
    make[MonadThrow[Future]].from((system: ActorSystem) => catsStdInstancesForFuture(system.dispatcher))
    make[Monad[Future]].using[MonadThrow[Future]]
    make[Applicative[Future]].using[Monad[Future]]
    make[Functor[Future]].using[Applicative[Future]]
  }

  def run: ServerBinding = {
    Injector().produceRun(module, Activation(Mode -> Mode.Prod)) {
      (routeProvider: AkkaApiRouteProvider, akkaSystem: ActorSystem, httpConfig: HttpConfig) =>
        implicit val system: ActorSystem = akkaSystem
        implicit val executionContext: ExecutionContextExecutor = system.dispatcher
        Await.result(
          for {
            server <- Http().newServerAt(httpConfig.host, httpConfig.port).bind(Route.seal(routeProvider.allRoutes))
              _ <- Future.never
          } yield server.addToCoordinatedShutdown(hardTerminationDeadline = 10.seconds)
          , Duration.Inf
        )
    }
  }
}
