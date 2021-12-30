package exchange.domain.service

import exchange.domain.entity.Token
import exchange.domain.infra.JwtComponentAlgebra

class AuthenticationService[F[_]](jwtComponentAlgebra: JwtComponentAlgebra[F]) {
  def createToken(username: String): F[Token] = jwtComponentAlgebra.createToken(username)

  def getUsername(token: Token): F[String] = jwtComponentAlgebra.getUsername(token)
}
