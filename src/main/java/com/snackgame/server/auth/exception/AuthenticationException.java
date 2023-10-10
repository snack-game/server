package com.snackgame.server.auth.exception;

public class AuthenticationException extends AuthException {

    public AuthenticationException() {
        super("사용자 인증에 실패했습니다");
    }
}
