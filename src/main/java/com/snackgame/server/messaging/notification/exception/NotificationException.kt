package com.snackgame.server.messaging.notification.exception

import com.snackgame.server.common.exception.BusinessException
import com.snackgame.server.common.exception.Kind

abstract class NotificationException(
    message: String,
    kind: Kind = Kind.BAD_REQUEST
) : BusinessException(kind, message)
