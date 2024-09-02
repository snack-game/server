package com.snackgame.server.game.snackgame.domain

import com.snackgame.server.game.snackgame.exception.NegativeCoordinateException


class Coordinate(val y: Int, val x: Int) {

    init {
        validateNonNegative(y)
        validateNonNegative(x)
    }


    private fun validateNonNegative(axisCoordinate: Int) {
        if (axisCoordinate < 0) {
            throw NegativeCoordinateException()
        }
    }

    override fun equals(o: Any?): Boolean {

        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val that = o as Coordinate

        return if (y != that.y) false else x == that.x
    }

    override fun hashCode(): Int {
        var result = y
        result = 31 * result + x
        return result
    }

    override fun toString(): String {
        return "Coordinate{" +
                "y=" + y +
                ", x=" + x +
                '}'
    }
}
