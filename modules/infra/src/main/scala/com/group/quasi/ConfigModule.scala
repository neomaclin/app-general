package com.group.quasi

import com.group.quasi.domain.infra.HttpConfig
import com.group.quasi.domain.model.users.UserConfig
import distage.ModuleDef
import pureconfig._
import pureconfig.generic.auto._

class ConfigModule extends ModuleDef{
  make[ConfigObjectSource].fromValue(ConfigSource.default)
//  make[EmailConfig].from((_:ConfigObjectSource).loadOrThrow[EmailConfig])
  make[HttpConfig].from((_:ConfigObjectSource).loadOrThrow[HttpConfig])
  //make[JwtConfig].from((_:ConfigObjectSource).loadOrThrow[JwtConfig])
  make[UserConfig].from((_:ConfigObjectSource).loadOrThrow[UserConfig])
}
