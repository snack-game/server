package com.snackgame.server.auth.exception;

public class TokenUnresolvableException extends AuthorizationException {
    public TokenUnresolvableException() {
        super("토큰을 읽지 못했습니다");
    }
}
