@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.game.snackgame.core.domain


import com.snackgame.server.game.snackgame.core.domain.snack.EmptySnack
import com.snackgame.server.game.snackgame.exception.InvalidCoordinateException
import com.snackgame.server.game.snackgame.fixture.BoardFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BoardTest {

    @Test
    fun `스낵을 제거하면 빈사과로 바뀐다`() {
        val board = BoardFixture.TWO_BY_FOUR()
        val streak = Streak(
            listOf(
                Coordinate(0, 0),
                Coordinate(1, 0)
            )
        )
        board.removeSnacksIn(streak)
        assertThat(streak.coordinates).allSatisfy { coordinate: Coordinate ->
            assertThat(board.getSnacks()[coordinate.y][coordinate.x])
                .isEqualTo(EmptySnack.get())
        }
    }

    @Test
    fun `스트릭은 게임판 범위 내에 속해야한다`() {
        val board = BoardFixture.TWO_BY_FOUR()
        val streak = Streak(
            listOf(
                Coordinate(9, 0),
                Coordinate(0, 9)
            )
        )
        assertThrows<InvalidCoordinateException> {
            board.removeSnacksIn(streak)
        }
    }
}
