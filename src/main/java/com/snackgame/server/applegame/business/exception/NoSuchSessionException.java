package com.snackgame.server.applegame.business.exception;

public class NoSuchSessionException extends AppleGameException {

    public NoSuchSessionException() {
        super("잘못된 세션입니다");
    }
}
