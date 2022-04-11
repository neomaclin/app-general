package com.group.quasi.domain.persistence

final case class DBConfig(
    username: String,
    password: String,
    url: String,
    profile: String,
    migrateOnStart: Boolean,
)
