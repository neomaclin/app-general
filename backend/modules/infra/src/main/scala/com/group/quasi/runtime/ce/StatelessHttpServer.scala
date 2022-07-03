package com.group.quasi.runtime.ce

trait StatelessHttpServer {

//
//  def run: ServerBinding = {
//    Injector().produceRun(new MainModule[Task] , Activation(Mode -> Mode.Prod)) {
//      (
//        routeProvider: AkkaApiRouteProvider,
//        akkaSystem: ActorSystem[SpawnProtocol.Command],
//        httpConfig: HttpConfig,
//        bootstrap: BootstrapService[Future],
//      ) =>
//        EmberServerBuilder
//          .default[Task]
//          .withHost(ipv4"0.0.0.0")
//          .withPort(port"8080")
//          .withHttpApp(httpApp)
//          .build
//    }
//  }

}
