package com.snackgame.server.applegame.business.domain.exception;

public abstract class AppleGameException extends RuntimeException {

    public AppleGameException(String message) {
        super(message);
    }
}
