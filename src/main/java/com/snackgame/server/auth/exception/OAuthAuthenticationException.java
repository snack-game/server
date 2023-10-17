package com.snackgame.server.auth.exception;

public class OAuthAuthenticationException extends AuthException {

    public OAuthAuthenticationException(String message) {
        super("소셜 사용자 인증 실패: " + message);
    }

    public OAuthAuthenticationException() {
        super("소셜 사용자 인증에 실패했습니다");
    }
}
