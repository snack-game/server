package com.snackgame.server.auth.exception;

public class TokenExpiredException extends AuthException {

    public TokenExpiredException() {
        this(Action.REISSUE);
    }

    private TokenExpiredException(Action action) {
        super(action, "토큰이 만료되었습니다");
    }

    public TokenExpiredException withLogoutAction() {
        return new TokenExpiredException(Action.LOGOUT);
    }
}
