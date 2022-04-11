package com.group.quasi.domain.persistence.operation


trait ChatRoomRepository[F[_]] {

  def register(roomId:String, creator:String, participants:List[String]): F[Unit]

  def loadBy(userId:String): F[List[String]]

}
