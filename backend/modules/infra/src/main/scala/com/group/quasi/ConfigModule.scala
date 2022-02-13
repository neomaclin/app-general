package com.group.quasi

import com.group.quasi.domain.infra.HttpConfig
import com.group.quasi.domain.model.users.UserConfig
import com.group.quasi.domain.persistence.repository.DBConfig
import distage.ModuleDef
import exchange.api.auth.JwtConfig
import pureconfig._
import pureconfig.generic.auto._

class ConfigModule extends ModuleDef {
  make[ConfigObjectSource].fromValue(ConfigSource.default)
//  make[EmailConfig].from((_:ConfigObjectSource).loadOrThrow[EmailConfig])
  make[HttpConfig].from((_: ConfigObjectSource).at("http").loadOrThrow[HttpConfig])
  //make[JwtConfig].from((_:ConfigObjectSource).at("").loadOrThrow[JwtConfig])
  make[UserConfig].from((_: ConfigObjectSource).at("user").loadOrThrow[UserConfig])
  make[DBConfig].from((_: ConfigObjectSource).at("db").loadOrThrow[DBConfig])
}
