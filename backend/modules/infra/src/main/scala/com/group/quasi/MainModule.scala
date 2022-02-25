package com.group.quasi

import com.group.quasi.service.ServiceModule
import com.softwaremill.id.{DefaultIdGenerator, IdGenerator}
import com.softwaremill.id.pretty.{PrettyIdGenerator, StringIdGenerator}
import distage.{ModuleDef, TagK}

class MainModule[F[+_]: TagK] extends ModuleDef {
  make[StringIdGenerator].fromValue(PrettyIdGenerator.singleNode)
  make[IdGenerator].from(new DefaultIdGenerator())
  include(new ConfigModule)
  include(new RepoModule)
  include(new RuntimeModule[F])
  include(new ServiceModule[F])
}
