package exchange.domain.services

import exchange.domain.entities.Token

trait AuthenticationService[F[_]] {
  def createToken(username: String): F[Token]

  def verifyToken(token: Token): F[Boolean]

  def getCredential(token: Token): F[String]
}
