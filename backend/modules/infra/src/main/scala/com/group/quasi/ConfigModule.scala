package com.group.quasi

import com.group.quasi.domain.infra.HttpConfig
import com.group.quasi.domain.infra.notification.{NotificationConfig, NotificationConfigs, NotificationOption}
import com.group.quasi.domain.infra.storage.{StorageConfig, StorageConfigs, StorageOption}
import com.group.quasi.domain.model.users.UserConfig
import com.group.quasi.domain.persistence.repository.DBConfig
import com.group.quasi.util.AsymKeys
import distage.ModuleDef
import exchange.api.auth.JwtConfig
import pdi.jwt.JwtAlgorithm
import pdi.jwt.algorithms.JwtAsymmetricAlgorithm
import pureconfig.ConfigConvert.catchReadError
import pureconfig._
import pureconfig.configurable._
import pureconfig.error.{CannotConvert, ConfigReaderFailures, ExceptionThrown}
import pureconfig.generic.auto._

import scala.util.Try

class ConfigModule extends ModuleDef {
  implicit val notificationOption: ConfigReader[NotificationOption] = ConfigReader[String].emap(catchReadError(NotificationOption.unsafe))
  implicit val notificationOptions: ConfigReader[Map[NotificationOption, NotificationConfig]] = genericMapReader[NotificationOption, NotificationConfig](catchReadError(NotificationOption.unsafe))
  implicit val storageOption: ConfigReader[StorageOption] =  ConfigReader[String].emap(catchReadError(StorageOption.unsafe))
  implicit val StorageOptions: ConfigReader[Map[StorageOption, StorageConfig]] = genericMapReader[StorageOption, StorageConfig](catchReadError(StorageOption.unsafe))

  implicit val jwtConfig: ConfigReader[JwtConfig] = ConfigReader.fromCursor[JwtConfig] { cur =>
    for{
      algo <- cur.fluent.at("algo").asString.flatMap(
        str => JwtAlgorithm.optionFromString(str)
          .filter(_.isInstanceOf[JwtAsymmetricAlgorithm])
          .map(_.asInstanceOf[JwtAsymmetricAlgorithm])
          .toRight[ConfigReaderFailures](ConfigReaderFailures(cur.failureFor(CannotConvert(str,"JwtAsymmetricAlgorithm",""))))
      )
      publicKey <- cur.fluent.at("public-key").asString.flatMap(
        str => Try{AsymKeys.fromAlgo(algo.fullName).publicKeyFromBase64String(str)}.toEither.left.map[ConfigReaderFailures](e=>ConfigReaderFailures(cur.failureFor(ExceptionThrown(e))))
      )
      privateKey <- cur.fluent.at("private-key").asString.flatMap(
        str => Try{AsymKeys.fromAlgo(algo.fullName).privateKeyFromBase64String(str)}.toEither.left.map[ConfigReaderFailures](e=>ConfigReaderFailures(cur.failureFor(ExceptionThrown(e))))
      )
    } yield {
      JwtConfig(publicKey = publicKey, privateKey = privateKey, algo = algo)
    }

  }
  make[ConfigObjectSource].fromValue(ConfigSource.default)
  make[NotificationConfigs].from((_:ConfigObjectSource).at("notification").loadOrThrow[NotificationConfigs])
  make[StorageConfigs].from((_:ConfigObjectSource).at("storage").loadOrThrow[StorageConfigs])
  make[HttpConfig].from((_: ConfigObjectSource).at("http").loadOrThrow[HttpConfig])
  make[JwtConfig].from((_:ConfigObjectSource).at("jwt").loadOrThrow[JwtConfig])
  make[UserConfig].from((_: ConfigObjectSource).at("user").loadOrThrow[UserConfig])
  make[DBConfig].from((_: ConfigObjectSource).at("persistence").loadOrThrow[DBConfig])
}
