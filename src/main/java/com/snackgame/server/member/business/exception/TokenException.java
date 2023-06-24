package com.snackgame.server.member.business.exception;

public abstract class TokenException extends RuntimeException {

    public TokenException(String message) {
        super(message);
    }
}
