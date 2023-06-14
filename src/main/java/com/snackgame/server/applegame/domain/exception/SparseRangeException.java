package com.snackgame.server.applegame.domain.exception;

public class SparseRangeException extends AppleGameException {

    public SparseRangeException() {
        super("좌표들이 완전하지 않습니다");
    }
}
