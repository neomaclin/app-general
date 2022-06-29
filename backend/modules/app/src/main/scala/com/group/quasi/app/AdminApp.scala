package com.group.quasi.app

import com.group.quasi.runtime.akka.StatefulHttpServer

object AdminApp extends StatefulHttpServer {
  def main(args: Array[String]): Unit = run
}
