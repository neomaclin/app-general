package com.group.quasi.api.social

//import com.group.quasi.api.auth.UserEndpoint.SecuredUserEndpoint
//import com.group.quasi.api.chat.{ChatRoom, ChatRooms}
//import sttp.capabilities.akka.AkkaStreams
//import sttp.model.StatusCode
//import sttp.tapir.json.circe.jsonBody
//import sttp.tapir.{endpoint, path, query, statusCode, streamBinaryBody}

object FriendZoneEndpoint {
  private val Friends = "friends"
//
//  class FriendsEndpointExt(val value: SecuredUserEndpoint) {
//    val find = endpoint.get
//      .in(Friends / "all")
//      .out(statusCode(StatusCode.Ok) and jsonBody[ChatRooms])
//      .errorOut(statusCode(StatusCode.NotFound))
//
//    val list = endpoint.get
//      .in(Friends / "all")
//      .out(statusCode(StatusCode.Ok) and jsonBody[ChatRooms])
//      .errorOut(statusCode(StatusCode.NotFound))
//
//    val request = endpoint.post
//      .in(Friends / "create")
//      .in(query[String]("by-user"))
//      .out(statusCode(StatusCode.Created) and jsonBody[ChatRoom])
//      .errorOut(statusCode(StatusCode.BadRequest))
//
//    val accept = endpoint.post
//      .in(Friends / "join")
//      .in(query[String]("room"))
//      .in(query[String]("user"))
//      .out(statusCode(StatusCode.Accepted) and jsonBody[ChatRoom])
//      .errorOut(statusCode(StatusCode.Forbidden))
//
//    val remove = endpoint.delete
//      .in(Friends / "remove")
//      .in(query[String]("user"))
//      .in(query[String]("room"))
//      .out(statusCode(StatusCode.Ok))
//      .errorOut(statusCode(StatusCode.Locked))


  //}
}
