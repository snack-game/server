package com.snackgame.server.applegame.domain.exception;

public class AppleNumberRangeException extends AppleGameException {

    public AppleNumberRangeException() {
        super("잘못된 범위의 숫자입니다");
    }
}
