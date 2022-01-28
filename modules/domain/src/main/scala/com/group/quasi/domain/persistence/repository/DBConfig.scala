package com.group.quasi.domain.persistence.repository

final case class DBConfig(
    profile: String,
    connectThreadPoolSize: Int,
)
