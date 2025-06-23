package com.snackgame.server.game.snackgame.core.domain

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

    fun toBombCoordinate(): List<Coordinate> {
        return AROUND_OFFSETS
            .mapNotNull { (dy, dx) ->
                val newY = y + dy
                val newX = x + dx
                if (newY >= 0 && newX >= 0) Coordinate(newY, newX) else null
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

    companion object {
        val AROUND_OFFSETS = listOf(
            -1 to -1, -1 to 0, -1 to 1,
            0 to -1, 0 to 0, 0 to 1,
            1 to -1, 1 to 0, 1 to 1
        )
    }
}
