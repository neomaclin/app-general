package exchange.domain.repository

import exchange.domain.entity.AccountOwner

trait AccountOwnerRepository[F[_]] {
  def create(username: String, password: String): F[AccountOwner]

  def getByUsername(username: String): F[Option[AccountOwner]]
}
