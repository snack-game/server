package com.snackgame.server.auth.exception;

public abstract class AuthorizationException extends RuntimeException {

    public AuthorizationException(String message) {
        super(message);
    }
}
