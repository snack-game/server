package com.snackgame.server.applegame.business.domain.exception;

public class GameSessionExpiredException extends AppleGameException {

    public GameSessionExpiredException(String message) {
        super(message);
    }
}
