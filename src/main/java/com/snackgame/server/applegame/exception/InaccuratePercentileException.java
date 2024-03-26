package com.snackgame.server.applegame.exception;

import com.snackgame.server.common.exception.Kind;

public class InaccuratePercentileException extends AppleGameException {

    public InaccuratePercentileException(double percentile) {
        super(Kind.INTERNAL_SERVER_ERROR, "백분위 계산이 잘못되었습니다: " + percentile);
    }
}
