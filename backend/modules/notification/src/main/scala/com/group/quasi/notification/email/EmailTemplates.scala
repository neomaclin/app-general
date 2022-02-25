package com.group.quasi.notification.email

import com.group.quasi.domain.infra.notification.SubjectContent

object EmailTemplates {

  def activateTemplate(title:String, content: String): SubjectContent = SubjectContent(title, content)
}
