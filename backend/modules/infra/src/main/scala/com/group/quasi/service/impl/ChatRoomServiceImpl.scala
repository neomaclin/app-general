package com.group.quasi.service.impl

import akka.actor.typed.{ActorSystem, SpawnProtocol}
import com.group.quasi.domain.service.ChatRoomService

class ChatRoomServiceImpl[F[_]](implicit akkaSystem: ActorSystem[SpawnProtocol.Command]) extends ChatRoomService[F] {
  // implicit private val timeout:Timeout = 3.seconds
  // implicit private val ex:ExecutionContextExecutor = akkaSystem.executionContext
//  private def fineChatRom(roomId:String):Future[Listing] = {
//    val key = ServiceKey[Action]("ChatroomKey"+roomId)
//    akkaSystem.receptionist.ask(Receptionist.Find(key,_)).map{
//      case key.Listing(listings) => listings.foreach(_ ! )
//   }
//
//  }
//
//  val  x = fineChatRom("111").map{
//
//  }

  override def init(byUserId: String): F[String] = ???

  override def terminate(roomId: String): F[Unit] = ???

  override def findRoomsHas(userId: String, size: Int): F[List[String]] = ???
}
