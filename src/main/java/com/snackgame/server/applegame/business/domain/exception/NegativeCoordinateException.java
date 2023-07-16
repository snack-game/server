package com.snackgame.server.applegame.business.domain.exception;

public class NegativeCoordinateException extends AppleGameException {

    public NegativeCoordinateException() {
        super("좌표는 음수가 아니어야 합니다");
    }
}
