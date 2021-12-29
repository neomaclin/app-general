package exchange.repository

import cats.Id
import exchange.domain.entity.AccountOwner
import exchange.domain.repository.AccountOwnerRepositoryAlgebra

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import scala.collection.mutable
import scala.jdk.CollectionConverters.MapHasAsScala

class AccountOwnerRepositoryInMemoryInterpreter extends AccountOwnerRepositoryAlgebra[Id] {
  private val map: mutable.Map[String, AccountOwner] = new ConcurrentHashMap[String, AccountOwner]().asScala

  override def create(username: String, password: String): Id[AccountOwner] = {
    val owner = AccountOwner(UUID.randomUUID().toString, username, password)
    map.put(username, owner)
    owner
  }

  override def getByUsername(username: String): Id[Option[AccountOwner]] =
    map.get(username)
}
