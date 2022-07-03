package com.group.quasi.domain

import com.group.quasi.domain.config.notification.NotificationConfigs

final case class AppConfig(
    notificationConfigs: NotificationConfigs,
)