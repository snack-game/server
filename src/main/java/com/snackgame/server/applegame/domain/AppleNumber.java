package com.snackgame.server.applegame.domain;

import java.util.concurrent.ThreadLocalRandom;

import com.snackgame.server.applegame.domain.exception.AppleNumberRangeException;

public class AppleNumber {

    public static final AppleNumber EMPTY = new AppleNumber();

    private static final int EMPTY_NUMBER = 0;

    private final int number;

    private AppleNumber() {
        this.number = EMPTY_NUMBER;
    }

    public AppleNumber(int number) {
        validateRangeOf(number);
        this.number = number;
    }

    public static AppleNumber ofRandomized() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int randomizedNumber = random.nextInt(1, 10);
        return new AppleNumber(randomizedNumber);
    }

    private void validateRangeOf(int number) {
        if (number < 1 || 9 < number) {
            throw new AppleNumberRangeException();
        }
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

        AppleNumber that = (AppleNumber)o;

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
