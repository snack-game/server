package com.snackgame.server.applegame.exception;

public class NegativeCoordinateException extends AppleGameException {

    public NegativeCoordinateException() {
        super("좌표는 음수가 아니어야 합니다");
    }
}
