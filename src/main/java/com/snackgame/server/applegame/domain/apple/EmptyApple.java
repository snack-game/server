package com.snackgame.server.applegame.domain.apple;

import com.snackgame.server.applegame.exception.EmptyAppleException;

public class EmptyApple extends Apple {

    private static final EmptyApple INSTANCE = new EmptyApple();

    private EmptyApple() {
        super();
    }

    public static EmptyApple get() {
        return INSTANCE;
    }

    @Override
    public GoldenApple golden() {
        throw new EmptyAppleException("빈 사과를 바꿀 수 없습니다");
    }

    @Override
    public boolean isGolden() {
        return false;
    }

    @Override
    public boolean exists() {
        return false;
    }
}
