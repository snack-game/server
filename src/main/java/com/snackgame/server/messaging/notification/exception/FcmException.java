package com.snackgame.server.messaging.notification.exception;

import com.snackgame.server.common.exception.Kind;

public class FcmException extends NotificationException {

    public FcmException() {
        super("잘못된 Fcm 접근입니다.", Kind.BAD_REQUEST);
    }

}
