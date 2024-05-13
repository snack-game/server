package com.snackgame.server.applegame.exception;

public class ScoreDecreasingException extends AppleGameException {

    public ScoreDecreasingException() {
        super("현재 점수보다 더 낮은 점수를 입력할 수 없습니다");
    }
}
