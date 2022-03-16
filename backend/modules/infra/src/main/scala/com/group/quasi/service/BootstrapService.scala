package com.group.quasi.service

trait BootstrapService[F[_]] {
  def initialize(): F[Unit]
}
