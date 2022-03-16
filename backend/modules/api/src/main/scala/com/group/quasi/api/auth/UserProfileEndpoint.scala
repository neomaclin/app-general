package com.group.quasi.api.auth

import com.group.quasi.api.auth.UserEndpoint.{SecuredUserEndpoint, UserPath}
import io.circe.generic.auto._
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody

object UserProfileEndpoint {
  private val ProfilePath = "profile"

  class UserProfileExt(val value: SecuredUserEndpoint) {
    val get = value.securedEndpoint.get
      .in(UserPath / ProfilePath)
      .out(jsonBody[UserProfile])

    val create = value.securedEndpoint.post
      .in(UserPath / ProfilePath)
      .in(jsonBody[UserProfile])

    val edit = value.securedEndpoint.put
      .in(UserPath / ProfilePath)
      .in(jsonBody[UserProfile])

  }

}
