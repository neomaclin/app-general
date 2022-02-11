package com.group.quasi

import com.group.quasi.runtime.akka.AkkaRuntimeModule
import distage.{ModuleDef, TagK}

class RuntimeModule[F[_]: TagK] extends ModuleDef {
  include(new AkkaRuntimeModule[F])
  // include(new Http4sRunTimeModule[Task])
}
