package exchange.domain.services

trait AccountOwnerService {
  def verifyAccountOwnerPassword(username: String, password: String): Boolean
}
