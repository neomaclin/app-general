package exchange.domain.service

import cats.Monad
import cats.implicits._
import exchange.domain.entity.AccountOwner
import exchange.domain.infra.PasswordEncoder
import exchange.domain.repository.AccountOwnerRepository

trait AccountOwnerService[F[_]] {
  def create(username: String, password: String): F[AccountOwner]

  def verifyUsernameAndPassword(username: String, password: String): F[Boolean]
}

object AccountOwnerService {
  def apply[F[_]: Monad](
      accountOwnerRepository: AccountOwnerRepository[F],
      passwordEncoder: PasswordEncoder[F]
  ): AccountOwnerService[F] =
    new AccountOwnerService[F] {
      override def create(username: String, password: String): F[AccountOwner] =
        for {
          p <- passwordEncoder.encode(password)
          a <- accountOwnerRepository.create(username, p)
        } yield a

      override def verifyUsernameAndPassword(username: String, password: String): F[Boolean] = {
        for {
          a <- accountOwnerRepository.getByUsername(username)
          m <- a.fold(false.pure[F]) { owner =>
            passwordEncoder.matches(password, owner.password)
          }
        } yield m
      }
    }
}
