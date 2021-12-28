package exchange.repository

import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.MongoConnection.ParsedURI
import reactivemongo.api.{AsyncDriver, DB, MongoConnection}

import scala.concurrent.{ExecutionContext, Future}

class MongoClient(mongoUri: String, dbName: String)(implicit ec: ExecutionContext) {
  private val parsedUri: Future[ParsedURI] = MongoConnection.fromString(mongoUri)
  private val driver: AsyncDriver = AsyncDriver()
  private val futureConnection: Future[MongoConnection] = parsedUri.flatMap(it => driver.connect(it))
  private val database: Future[DB] = futureConnection.flatMap(_.database(dbName))

  def getCollectionFuture(collection: String): Future[BSONCollection] = database.map(_.collection(collection))
}
