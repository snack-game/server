package com.snackgame.server.applegame.business.exception;

import com.snackgame.server.common.exception.BusinessException;

public abstract class AppleGameException extends BusinessException {

    public AppleGameException(String message) {
        super(message);
    }
}
