package com.snackgame.server.applegame.business.domain;

import java.util.concurrent.ThreadLocalRandom;

import com.snackgame.server.applegame.business.exception.AppleNumberRangeException;

public class Apple {

    public static final Apple EMPTY = new Apple();

    private static final int EMPTY_NUMBER = 0;

    private final int number;
    private final boolean isGolden;

    private Apple() {
        this.number = EMPTY_NUMBER;
        this.isGolden = false;
    }

    public Apple(int number) {
        this(number, false);
    }

    private Apple(int number, boolean isGolden) {
        validateRangeOf(number);
        this.number = number;
        this.isGolden = isGolden;
    }

    public static Apple ofRandomizedNumber() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int randomizedNumber = random.nextInt(1, 10);
        return new Apple(randomizedNumber);
    }

    private void validateRangeOf(int number) {
        if (number < 1 || 9 < number) {
            throw new AppleNumberRangeException();
        }
    }

    public Apple golden() {
        return new Apple(number, true);
    }

    public boolean isGolden() {
        return isGolden;
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "(" + number + ")";
    }
}
