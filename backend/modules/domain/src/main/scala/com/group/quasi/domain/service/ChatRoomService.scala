package com.group.quasi.domain.service

trait ChatRoomService[F[_]] {

  def init(byUserLogin:String):F[String]

  def terminate(roomName:String):F[Unit]

  def findRoomsHas(userLogin:String, size:Int):F[List[String]]


}
