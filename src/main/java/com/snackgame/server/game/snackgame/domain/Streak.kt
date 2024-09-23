package com.snackgame.server.game.snackgame.domain

import com.snackgame.server.game.snackgame.exception.InvalidStreakException
import kotlin.math.abs

class Streak(val coordinates: List<Coordinate>) {

    val length = coordinates.size

    init {
        validateLengthOf(coordinates)
        validateNoDuplicateIn(coordinates)
        validateDirectionsOf(coordinates)
    }

    private fun validateLengthOf(coordinates: List<Coordinate>) {
        if (coordinates.size < 2) {
            throw InvalidStreakException() // TODO: 오류 메시지 상세화, 로깅
        }
    }

    private fun validateNoDuplicateIn(coordinates: List<Coordinate>) {
        if (coordinates.distinct().size != coordinates.size) {
            throw InvalidStreakException()
        }
    }

    private fun validateDirectionsOf(coordinates: List<Coordinate>) {
        for (i in 0 until coordinates.size - 1) {
            if (!isStraightOrDiagonal(coordinates[i], coordinates[i + 1])) {
                throw InvalidStreakException()
            }
        }
    }

    private fun isStraightOrDiagonal(one: Coordinate, other: Coordinate): Boolean {
        val yDif = abs(other.y - one.y)
        val xDif = abs(other.x - one.x)

        if (yDif == 0 && xDif == 0) {
            return false
        }
        return yDif * xDif == 0 || yDif == xDif
    }
}
