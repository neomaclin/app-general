package com.group.quasi.runtime.akka.route

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class AkkaApiRouteProvider(
    fileRoutes: FileRoutes,
    userRoutes: UserRoutes,
    heartbeatRoutes: HeartbeatRoutes
) {

  def allRoutes: Route =
    userRoutes.signupRoutes ~ userRoutes.loginRoute ~ userRoutes.profileRoutes ~
    fileRoutes.fileUploadRoute ~ fileRoutes.fileDownloadRoute ~ heartbeatRoutes.ping

}
