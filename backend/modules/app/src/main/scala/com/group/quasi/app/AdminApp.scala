package com.group.quasi.app

import com.group.quasi.runtime.akka.AkkaHttpServer

object AdminApp extends AkkaHttpServer {
  def main(args: Array[String]): Unit = run
}
