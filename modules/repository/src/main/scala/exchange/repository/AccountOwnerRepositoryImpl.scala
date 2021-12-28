package exchange.repository

import exchange.domain.entity.AccountOwner
import exchange.domain.repository.AccountOwnerRepository

class AccountOwnerRepositoryImpl[F[_]](mongoClient: MongoClient) extends AccountOwnerRepository[F] {
  override def create(username: String, password: String): F[AccountOwner] = ???

  override def getByUsername(username: String): F[Option[AccountOwner]] = ???
}
