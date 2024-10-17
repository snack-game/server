package com.snackgame.server.messaging.notification.exception

import com.snackgame.server.common.exception.Kind

class DuplicatedDeviceException : NotificationException("이미 등록된 기기입니다", Kind.IGNORE)
