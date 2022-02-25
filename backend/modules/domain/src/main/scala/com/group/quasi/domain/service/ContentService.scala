package com.group.quasi.domain.service

import com.group.quasi.domain.model.contents.Content

trait ContentService[F[_]] {
  def pull(userId: String, roomId: String):F[List[Content]]
  def persist(userId:String, roomId: String, content: Content):F[Content.Id]
}