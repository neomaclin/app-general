package exchange.repository

import scala.concurrent.ExecutionContext

class MongoClient(mongoUri: String, dbName: String)(implicit ec: ExecutionContext) {}
