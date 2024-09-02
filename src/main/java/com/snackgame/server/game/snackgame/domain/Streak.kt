package com.snackgame.server.game.snackgame.domain

import com.snackgame.server.game.snackgame.exception.InvalidStreakException
import kotlin.math.abs

class Streak(private val coordinates: MutableList<Coordinate>) {

    fun toCoordinates(): MutableList<Coordinate> {
        return coordinates
    }

    fun validateStreak() {
        for (i in 0 until coordinates.size - 1) {
            if (!compareDirections(coordinates[i], coordinates[i + 1])) throw InvalidStreakException()
        }
    }

    private fun compareDirections(before: Coordinate, after: Coordinate): Boolean {
        val yDif = abs(after.y - before.y)
        val xDif = abs(after.x - before.x)

        return yDif <= 1 && xDif <= 1
    }


}
