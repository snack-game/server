package com.snackgame.server.auth.exception;

public abstract class TokenException extends RuntimeException {

    public TokenException(String message) {
        super(message);
    }
}
