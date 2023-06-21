package com.snackgame.server.member.exception;

public abstract class TokenException extends RuntimeException {

    public TokenException(String message) {
        super(message);
    }
}
