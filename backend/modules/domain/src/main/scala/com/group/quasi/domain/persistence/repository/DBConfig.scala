package com.group.quasi.domain.persistence.repository

final case class DBConfig(
    username: String,
    password: String,
    url: String,
    profile: String,
    connectThreadPoolSize: Int,
    migrateOnStart: Boolean,
)
