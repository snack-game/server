package com.snackgame.server.auth.exception;

public class JwkRenewalFailureException extends AuthException {

    public JwkRenewalFailureException(String message) {
        super(Action.NONE, message);
    }
}
