package com.snackgame.server.auth.exception;

public class TokenExpiredException extends AuthException {

    public TokenExpiredException() {
        super("토큰이 만료되었습니다");
    }
}
