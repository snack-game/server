package com.snackgame.server.auth.exception;

public class InvalidTokenException extends AuthException {

    public InvalidTokenException() {
        super("토큰이 유효하지 않습니다");
    }
}
