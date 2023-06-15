package com.snackgame.server.applegame.domain;

import com.snackgame.server.applegame.domain.exception.NegativeCoordinateException;

public class Coordinate {

    private final int y;
    private final int x;

    public Coordinate(int y, int x) {
        validateNonNegative(y);
        validateNonNegative(x);
        this.y = y;
        this.x = x;
    }

    private void validateNonNegative(int axisCoordinate) {
        if (axisCoordinate < 0) {
            throw new NegativeCoordinateException();
        }
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Coordinate that = (Coordinate)o;

        if (y != that.y)
            return false;
        return x == that.x;
    }

    @Override
    public int hashCode() {
        int result = y;
        result = 31 * result + x;
        return result;
    }
}
