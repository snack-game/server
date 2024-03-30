package com.snackgame.server.applegame.exception;

import com.snackgame.server.common.exception.BusinessException;
import com.snackgame.server.common.exception.Kind;

public abstract class AppleGameException extends BusinessException {

    public AppleGameException(String message) {
        super(message);
    }

    public AppleGameException(Kind kind, String message) {
        super(kind, message);
    }
}
