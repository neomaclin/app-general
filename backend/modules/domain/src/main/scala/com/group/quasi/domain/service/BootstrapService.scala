package com.group.quasi.domain.service

trait BootstrapService[F[_]] {
  def initialize(): F[Unit]
}
