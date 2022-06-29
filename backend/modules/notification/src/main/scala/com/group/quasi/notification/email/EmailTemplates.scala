package com.group.quasi.notification.email

import com.group.quasi.domain.config.notification.SubjectContent

object EmailTemplates {

  def activateTemplate(title:String, content: String): SubjectContent = SubjectContent(title, content)
}
