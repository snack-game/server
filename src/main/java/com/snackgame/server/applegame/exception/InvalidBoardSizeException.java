package com.snackgame.server.applegame.exception;

public class InvalidBoardSizeException extends AppleGameException {

    public InvalidBoardSizeException() {
        super("잘못된 크기의 게임판입니다");
    }
}
