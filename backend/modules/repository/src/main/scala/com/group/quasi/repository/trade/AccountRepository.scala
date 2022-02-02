package com.group.quasi.repository.trade

import akka.actor.ActorSystem
import com.group.quasi.domain.persistence.repository.DBConfig
import com.group.quasi.repository.SlickRepository

class AccountRepository(override val config: DBConfig)(implicit override val system: ActorSystem) extends SlickRepository {



}
