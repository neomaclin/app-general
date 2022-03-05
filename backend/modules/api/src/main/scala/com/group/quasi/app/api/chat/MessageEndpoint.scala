package com.group.quasi.app.api.chat

import sttp.model.StatusCode
import sttp.tapir._

object MessageEndpoint {

  private val ChatPath = "chat"

  val create = endpoint.post.in(ChatPath / "post")
    .in(query[String]("user-id"))
    .out(statusCode(StatusCode.Accepted) and emptyOutput)

  val edit = endpoint.put.in(ChatPath / "edit")
    .in(query[String]("id"))
    .in(query[String]("user"))
    .out(statusCode(StatusCode.Accepted) and emptyOutput)

  val delete = endpoint.delete.in(ChatPath / "delete")
    .in(query[String]("id"))
    .in(query[String]("user"))
    .out(statusCode(StatusCode.Accepted) and emptyOutput)

  val batch = endpoint.get
    .in(ChatPath)
    .in(query[String]("size"))
    .in(query[String]("last-id"))
   // .out(jsonBody[ChatBatch])

//  val stream = endpoint.get
//    .in(ChatPath)
//    .in(query[String]("room"))
//    .out()
}
