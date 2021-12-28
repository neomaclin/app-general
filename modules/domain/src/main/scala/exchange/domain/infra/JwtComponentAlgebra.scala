package exchange.domain.infra

import exchange.domain.entity.Token

trait JwtComponentAlgebra[F[_]] {
  def createToken(username: String): F[Token]

  def getUsername(token: Token): F[String]
}
