package com.snackgame.server.auth.exception;

public class TokenInvalidException extends AuthException {

    public TokenInvalidException() {
        super("토큰이 유효하지 않습니다");
    }
}
