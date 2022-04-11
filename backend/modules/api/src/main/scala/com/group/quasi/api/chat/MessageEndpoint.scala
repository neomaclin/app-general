package com.group.quasi.api.chat

import com.group.quasi.api.auth.UserEndpoint.SecuredUserEndpoint
import sttp.model.StatusCode
import sttp.tapir._

object MessageEndpoint {

  private val MessagePath = "msg"
  class MessageEndpointExt(val value: SecuredUserEndpoint) {

    val create = endpoint.post.in(MessagePath / "post")
      .in(query[String]("user-id"))
      .out(statusCode(StatusCode.Accepted) and emptyOutput)

    val edit = endpoint.put.in(MessagePath / "edit")
      .in(query[String]("id"))
      .in(query[String]("user"))
      .out(statusCode(StatusCode.Accepted) and emptyOutput)

    val delete = endpoint.delete.in(MessagePath / "delete")
      .in(query[String]("id"))
      .in(query[String]("user"))
      .out(statusCode(StatusCode.Accepted) and emptyOutput)

    val batch = endpoint.get
      .in(MessagePath)
      .in(query[String]("size"))
      .in(query[String]("last-id"))
  }
}
