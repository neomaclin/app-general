package com.group.quasi.app.api.chat
import io.circe.generic.auto.exportEncoder
import sttp.capabilities.akka.AkkaStreams
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import io.circe.generic.auto._

object ChatroomEndpoint {

  private val ChatRoomPath = "rooms"

  val list = endpoint.get
    .in(ChatRoomPath / "all")
    .out(statusCode(StatusCode.Ok) and jsonBody[ChatRooms])
    .errorOut(statusCode(StatusCode.NotFound))

  val create = endpoint.post
    .in(ChatRoomPath / "create")
    .in(query[String]("by-user"))
    .out(statusCode(StatusCode.Created) and jsonBody[ChatRoom])
    .errorOut(statusCode(StatusCode.BadRequest))

  val join = endpoint.post
    .in(ChatRoomPath / "join")
    .in(query[String]("room"))
    .in(query[String]("user"))
    .out(statusCode(StatusCode.Accepted) and jsonBody[ChatRoom])
    .errorOut(statusCode(StatusCode.Forbidden))

  val leave = endpoint.delete
    .in(ChatRoomPath / "leave")
    .in(query[String]("room"))
    .in(query[String]("user"))
    .out(statusCode(StatusCode.Ok))
    .errorOut(statusCode(StatusCode.Locked))

  val close = endpoint.delete
    .in(ChatRoomPath / "close")
    .in(query[String]("room"))
    .out(statusCode(StatusCode.Ok))
    .errorOut(statusCode(StatusCode.Locked))


  val remove = endpoint.delete
    .in(ChatRoomPath / "remove")
    .in(query[String]("user"))
    .in(query[String]("room"))
    .out(statusCode(StatusCode.Ok))
    .errorOut(statusCode(StatusCode.Locked))


  val live = endpoint.get
    .in(ChatRoomPath)
    .in(path[String]("room"))
    .out(streamBinaryBody(AkkaStreams))

}
