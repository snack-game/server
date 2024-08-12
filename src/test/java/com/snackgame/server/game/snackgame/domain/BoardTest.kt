package com.snackgame.server.game.snackgame.domain


import com.snackgame.server.game.snackgame.exception.InvalidCoordinateException
import com.snackgame.server.game.snackgame.fixture.TestFixture
import com.snackgame.server.game.snackgame.snack.EmptySnack
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BoardTest {

    @Test
    fun 스낵을_제거하면_빈사과로_바뀐다() {
        val board = TestFixture.TWO_BY_FOUR()
        val streak = Streak(
            arrayListOf(
                Coordinate(0, 0),
                Coordinate(1, 0)
            )
        )
        board.removeSnacksIn(streak)
        assertThat(streak.toCoordinates()).allSatisfy { coordinate: Coordinate ->
            assertThat(board.getSnacks()[coordinate.y][coordinate.x])
                .isEqualTo(EmptySnack.get())
        }
    }

    @Test
    fun 스트릭은_게임판_범위내에_속해야한다() {
        val board = TestFixture.TWO_BY_FOUR()
        val streak = Streak(
            arrayListOf(
                Coordinate(9, 0),
                Coordinate(0, 9)
            )
        )
        assertThrows<InvalidCoordinateException> {
            board.removeSnacksIn(streak)
        }
    }
}