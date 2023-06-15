package com.snackgame.server.applegame.domain.exception;

public abstract class AppleGameException extends RuntimeException {

    public AppleGameException(String message) {
        super(message);
    }
}
