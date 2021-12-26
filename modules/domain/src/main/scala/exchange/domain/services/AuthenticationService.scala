package exchange.domain.services

import exchange.domain.entities.Token

trait AuthenticationService {
  def createToken(username: String): Token

  def verifyToken(token: Token): Token

  def getCredential(token: Token): String
}
