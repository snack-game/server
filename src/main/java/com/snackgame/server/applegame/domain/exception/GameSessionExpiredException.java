package com.snackgame.server.applegame.domain.exception;

public class GameSessionExpiredException extends AppleGameException {

    public GameSessionExpiredException(String message) {
        super(message);
    }
}
