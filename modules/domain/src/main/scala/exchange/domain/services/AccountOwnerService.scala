package exchange.domain.services

trait AccountOwnerService[F[_]] {
  def verifyAccountOwnerPassword(username: String, password: String): F[Boolean]
}
