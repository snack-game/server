package com.snackgame.server.applegame.business.domain;

import com.snackgame.server.applegame.business.exception.AppleNumberRangeException;

public abstract class AnyApple {

    protected static final int NUMBER_MINIMUM = 1;
    protected static final int NUMBER_MAXIMUM = 9;

    private final int number;

    protected AnyApple() {
        this.number = 0;
    }

    protected AnyApple(int number) {
        validateRangeOf(number);
        this.number = number;
    }

    private void validateRangeOf(int number) {
        if (NUMBER_MINIMUM <= number && number <= NUMBER_MAXIMUM) {
            return;
        }
        throw new AppleNumberRangeException();
    }

    public abstract GoldenApple golden();

    public abstract boolean isGolden();

    public boolean isEmpty() {
        return false;
    }

    public boolean exists() {
        return !isEmpty();
    }

    public int getNumber() {
        return number;
    }
}
