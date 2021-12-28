package exchange.domain.service

import cats.Monad
import cats.implicits._
import exchange.domain.entity.AccountOwner
import exchange.domain.infra.PasswordEncoderAlgebra
import exchange.domain.repository.AccountOwnerRepositoryAlgebra

class AccountOwnerService[F[_]: Monad](
                                        accountOwnerRepository: AccountOwnerRepositoryAlgebra[F],
                                        passwordEncoder: PasswordEncoderAlgebra[F],
) {
  def create(username: String, password: String): F[AccountOwner] =
    for {
      p <- passwordEncoder.encode(password)
      a <- accountOwnerRepository.create(username, p)
    } yield a

  def verifyUsernameAndPassword(username: String, password: String): F[Boolean] =
    for {
      a <- accountOwnerRepository.getByUsername(username)
      m <- a.fold(false.pure[F]) { owner =>
        passwordEncoder.matches(password, owner.password)
      }
    } yield m
}

object AccountOwnerService {
  def apply[F[_]: Monad](
                          accountOwnerRepository: AccountOwnerRepositoryAlgebra[F],
                          passwordEncoder: PasswordEncoderAlgebra[F],
  ): AccountOwnerService[F] =
    new AccountOwnerService[F](accountOwnerRepository, passwordEncoder)
}
