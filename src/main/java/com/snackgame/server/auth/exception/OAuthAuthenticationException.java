package com.snackgame.server.auth.exception;

public class OAuthAuthenticationException extends AuthException {

    public OAuthAuthenticationException(String message) {
        super(Action.NONE, "소셜 사용자 인증 실패: " + message);
    }
}
