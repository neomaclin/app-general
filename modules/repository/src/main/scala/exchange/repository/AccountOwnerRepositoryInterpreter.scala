package exchange.repository

import cats.data.State
import exchange.domain.entity.AccountOwner
import exchange.domain.repository.AccountOwnerRepositoryAlgebra

import java.util.UUID

object AccountOwnerRepositoryInterpreter {
  type AccountOwnerMap = Map[String, AccountOwner]

  type AccountOwnerState[A] = State[AccountOwnerMap, A]

  def apply: AccountOwnerRepositoryAlgebra[AccountOwnerState] =
    new AccountOwnerRepositoryAlgebra[AccountOwnerState] {
      override def create(username: String, password: String): AccountOwnerState[AccountOwner] =
        State { owners =>
          val owner = AccountOwner(UUID.randomUUID.toString, username, password)
          (owners + (username -> owner), owner)
        }

      override def getByUsername(username: String): AccountOwnerState[Option[AccountOwner]] =
        State.inspect { owners =>
          owners.get(username)
        }
    }
}
