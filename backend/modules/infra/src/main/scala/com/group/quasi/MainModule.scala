package com.group.quasi

import com.softwaremill.id.pretty.{PrettyIdGenerator, StringIdGenerator}
import distage.{ModuleDef, TagK}

class MainModule[F[+_]: TagK] extends ModuleDef {
  make[StringIdGenerator].fromValue(PrettyIdGenerator.singleNode)
  include(new ConfigModule)
  include(new RepoModule)
  include(new RuntimeModule[F])
}
