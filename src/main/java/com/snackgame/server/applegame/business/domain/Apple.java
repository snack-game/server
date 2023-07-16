package com.snackgame.server.applegame.business.domain;

import java.util.concurrent.ThreadLocalRandom;

import com.snackgame.server.applegame.business.exception.AppleNumberRangeException;

public class Apple {

    public static final Apple EMPTY = new Apple();

    private static final int EMPTY_NUMBER = 0;

    private final int number;

    private Apple() {
        this.number = EMPTY_NUMBER;
    }

    public Apple(int number) {
        validateRangeOf(number);
        this.number = number;
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

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    public boolean exists() {
        return !isEmpty();
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Apple that = (Apple)o;

        return number == that.number;
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public String toString() {
        return "(" + number + ")";
    }
}
