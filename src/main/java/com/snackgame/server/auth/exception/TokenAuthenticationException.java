package com.snackgame.server.auth.exception;

public class TokenAuthenticationException extends AuthException {

    public TokenAuthenticationException() {
        super(Action.NONE, "사용자 인증에 실패했습니다");
    }
}
