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
        val streak = Streak.of(
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
        val streak = Streak.of(
            listOf(
                Coordinate(9, 0),
                Coordinate(0, 9)
            )
        )
        assertThrows<InvalidCoordinateException> {
            board.removeSnacksIn(streak)
        }
    }

    @Test
    fun `폭탄은 유효한 좌표의 스낵만 제거한다`() {
        val board = BoardFixture.THREE_BY_FOUR()
        val streak = Streak.bomb(
            listOf(
                Coordinate(0, 0),
                Coordinate(1, 0),
                Coordinate(9, 9)
            )
        )

        val removedSnacks = board.bombSnacksIn(streak)

        assertThat(removedSnacks).hasSize(2)
        assertThat(board.getSnacks()[0][0]).isEqualTo(EmptySnack.get())
        assertThat(board.getSnacks()[1][0]).isEqualTo(EmptySnack.get())
    }

    @Test
    fun `모든 좌표가 게임판 밖이면 아무것도 제거되지 않는다`() {
        val board = BoardFixture.TWO_BY_FOUR()
        val streak = Streak.bomb(
            listOf(
                Coordinate(8, 8),
                Coordinate(9, 9)
            )
        )

        val removedSnacks = board.bombSnacksIn(streak)

        assertThat(removedSnacks).isEmpty()

        assertThat(board.getSnacks()[0][0]).isNotEqualTo(EmptySnack.get())
        assertThat(board.getSnacks()[1][0]).isNotEqualTo(EmptySnack.get())
    }

    @Test
    fun `폭탄으로 제거된 스낵은 빈 스낵으로 바뀐다`() {
        val board = BoardFixture.TWO_BY_FOUR()
        val streak = Streak.bomb(
            listOf(
                Coordinate(0, 1),
                Coordinate(1, 1)
            )
        )

        val removedSnacks = board.bombSnacksIn(streak)

        assertThat(removedSnacks).hasSize(2)
        assertThat(board.getSnacks()[0][1]).isEqualTo(EmptySnack.get())
        assertThat(board.getSnacks()[1][1]).isEqualTo(EmptySnack.get())
    }
}
