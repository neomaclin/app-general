package com.group.quasi.domain.model

package object roles {
  sealed trait Role
  case object Member extends Role
  case object Admin extends Role

}
