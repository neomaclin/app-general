package com.group.quasi.api.heartbeat

import io.circe.Encoder
import sttp.tapir.Endpoint
import sttp.tapir._
import sttp.tapir.json.circe.jsonBody
import io.circe._
import io.circe.generic.semiauto._


import java.time.ZoneId
import scala.util.Try

object HeartbeatEndpoint {
  implicit val nodeStatusSchema: Schema[NodeStatus] = Schema.schemaForString.map(_=>None)(_.toString)
  implicit val nodeStatusDecoder: Decoder[NodeStatus] = Decoder.decodeString.map(_=>Fine)
  implicit val nodeStatusEncoder: Encoder[NodeStatus] = Encoder.encodeString.contramap(_.toString.toLowerCase)
  implicit val zoneIdSchema: Schema[ZoneId] = Schema.schemaForString.map(str => Try(ZoneId.of(str.toUpperCase)).toOption)(_.toString)
  implicit val zoneIdDecoder: Decoder[ZoneId] = Decoder.decodeString.map(str => Try(ZoneId.of(str.toUpperCase)).toOption.getOrElse(ZoneId.systemDefault()))
  implicit val zoneIdEncoder: Encoder[ZoneId] = Encoder.encodeString.contramap(_.toString.toLowerCase)
  implicit val pongSchema: Schema[Pong] = Schema.derived
  implicit val pongDecoder: Decoder[Pong] = deriveDecoder
  implicit val pongEncoder: Encoder[Pong] = deriveEncoder
  val HeartbeatPath = "heartbeat"

  val ping: Endpoint[Unit, Unit, Unit, Pong, Any] = endpoint.get
    .in(HeartbeatPath / "ping")
    .out(jsonBody[Pong])

}
