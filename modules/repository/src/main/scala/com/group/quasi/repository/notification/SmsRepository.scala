package com.group.quasi.repository.notification

import akka.actor.ActorSystem
import com.group.quasi.domain.persistence.repository.DBConfig
import com.group.quasi.repository.{PostgresProfile, SlickRepository}

class SmsRepository(override val config: DBConfig, val postgresProfile: PostgresProfile)(implicit override val system: ActorSystem) extends SlickRepository {


}