package com.snackgame.server.messaging.notification.exception;

import com.snackgame.server.common.exception.Kind;

public class InvalidMessageException extends NotificationException {

    public InvalidMessageException() {
        super("유효하지 않은 메세지입니다.", Kind.BAD_REQUEST);
    }

}