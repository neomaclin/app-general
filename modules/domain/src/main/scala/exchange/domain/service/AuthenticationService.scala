package exchange.domain.service

import exchange.domain.entity.Token

trait AuthenticationService[F[_]] {
  def createToken(username: String): F[Token]

  def verifyToken(token: Token): F[Boolean]

  def getCredential(token: Token): F[String]
}

object AuthenticationService {
  def apply[F[_]]: AuthenticationService[F] =
    new AuthenticationService[F] {
      override def createToken(username: String): F[Token] = ???

      override def verifyToken(token: Token): F[Boolean] = ???

      override def getCredential(token: Token): F[String] = ???
    }
}
