package com.snackgame.server.messaging.push.exception;

import com.snackgame.server.common.exception.Kind;

public class FCMException extends NotificationException {

    public FCMException() {
        super("잘못된 FCM 접근입니다.", Kind.INTERNAL_SERVER_ERROR);
    }

}
