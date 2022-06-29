package com.group.quasi.domain.persistence.operation


import com.group.quasi.domain.model.users.UserId


trait ChatRoomRepository[F[_]] {

  def register(roomId:String, creator:UserId, participants:List[UserId]): F[Unit]

  def loadBy(userId:UserId): F[List[String]]

  def updateRoomName(oldRoomId:String, newRoomId: String, byUser:UserId): F[Unit]


}
