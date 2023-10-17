package com.snackgame.server.auth.exception;

public class TokenAuthenticationException extends AuthException {

    public TokenAuthenticationException() {
        super("사용자 인증에 실패했습니다");
    }
}
