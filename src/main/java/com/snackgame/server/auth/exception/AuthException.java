package com.snackgame.server.auth.exception;

public abstract class AuthException extends RuntimeException {

    public enum Action {NONE, REISSUE, LOGOUT}

    private final Action action;

    public AuthException(Action action, String message) {
        super(message);
        this.action = action;
    }

    public String getAction() {
        if (action.equals(Action.NONE)) {
            return null;
        }
        return action.name();
    }
}
