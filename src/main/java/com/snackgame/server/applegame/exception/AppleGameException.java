package com.snackgame.server.applegame.exception;

import com.snackgame.server.common.exception.BusinessException;

public abstract class AppleGameException extends BusinessException {

    public AppleGameException(String message) {
        super(message);
    }
}
