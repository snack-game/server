package com.snackgame.server.auth.exception;

public abstract class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }
}
