package com.snackgame.server.applegame.business.exception;

public class NotOwnedException extends AppleGameException {

    public NotOwnedException() {
        super("사용자의 세션이 아닙니다");
    }
}
