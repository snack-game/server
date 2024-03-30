package com.snackgame.server.applegame.exception;

import com.snackgame.server.common.exception.Kind;

public class SessionNotFinishedException extends AppleGameException {

    public SessionNotFinishedException() {
        super(Kind.INTERNAL_SERVER_ERROR, "세션이 아직 종료되지 않았습니다");
    }
}
