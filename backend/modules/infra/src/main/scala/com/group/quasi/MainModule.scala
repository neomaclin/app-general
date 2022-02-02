package com.group.quasi

import distage.{ModuleDef, TagK}

class MainModule[F[+_]:TagK] extends ModuleDef{
  include(new ConfigModule)
  include(new RepoModule)
  include(new RuntimeModule[F])
}
