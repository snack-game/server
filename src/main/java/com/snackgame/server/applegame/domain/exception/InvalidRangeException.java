package com.snackgame.server.applegame.domain.exception;

public class InvalidRangeException extends AppleGameException {

    public InvalidRangeException() {
        super("범위가 잘못 되었습니다");
    }
}