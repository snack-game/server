package com.snackgame.server.messaging.notification.exception;

import com.snackgame.server.common.exception.Kind;

public class MessageNotSentException extends NotificationException {

    public MessageNotSentException() {
        super("메세지가 전송되지 않았습니다.", Kind.BAD_REQUEST);
    }

}
