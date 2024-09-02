package com.snackgame.server.game.snackgame.domain

import com.snackgame.server.game.snackgame.exception.InvalidCoordinateException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class StreakTest {

    @Test
    fun 스트릭은_상하좌우대각으로_연속되어야_한다() {
        val wrongStreak = Streak(
            arrayListOf(
                Coordinate(0, 0),
                Coordinate(3, 0),
                Coordinate(4, 2),
            )
        )
        assertThrows<InvalidCoordinateException> {
            wrongStreak.validateStreak()
        }

        val properStreak = Streak(
            arrayListOf(
                Coordinate(0, 0),
                Coordinate(1, 0),
                Coordinate(2, 1),
            )
        )
        assertDoesNotThrow { properStreak.validateStreak() }
    }
}
