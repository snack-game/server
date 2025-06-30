package com.snackgame.server.game.snackgame.core.domain


import com.snackgame.server.game.snackgame.fixture.BoardFixture
import com.snackgame.server.game.snackgame.fixture.BoardFixture.TWO_BY_TWO_WITH_GOLDEN_SNACK
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SnackgameTest {

    @Test
    fun 게임을_6_X_8크기의_게임판으로_생성한다() {
        val game = Snackgame(땡칠().id)
        assertThat(game.board.getSnacks()).hasSize(8)
        assertThat(game.board.getSnacks()).allSatisfy { row ->
            assertThat(row).hasSize(6)
        }
    }

    @Test
    fun 스낵을_제거하면_점수를_얻을_수_있을() {
        val game = Snackgame(땡칠().id, BoardFixture.TWO_BY_FOUR())
        val streak = Streak.of(
            arrayListOf(
                Coordinate(0, 0),
                Coordinate(1, 0)
            )
        )
        game.remove(streak)
        assertThat(game.score).isEqualTo(2)
    }

    @Test
    fun 황금스낵를_제거해도_점수는_초기화되지_않는다() {
        val game = Snackgame(땡칠().id, BoardFixture.TWO_BY_TWO_WITH_GOLDEN_SNACK())
        val streak = Streak.of(
            arrayListOf(
                Coordinate(0, 0),
                Coordinate(1, 0)
            )
        )
        val expectedScore = game.score + 2
        game.remove(streak)
        assertThat(game.score).isEqualTo(expectedScore)
    }

    @Test
    fun `폭탄으로 황금 스낵을 제거하면 보드가 초기화된다`() {
        val game = Snackgame(땡칠().id, TWO_BY_TWO_WITH_GOLDEN_SNACK())

        Streak.of(
            listOf(
                Coordinate(0, 0),
                Coordinate(0, 1),
                Coordinate(1, 1),
                Coordinate(1, 0)
            )
        ).let { game.removeBomb(it) }

        assertThat(game.board).isNotEqualTo(TWO_BY_TWO_WITH_GOLDEN_SNACK())
    }
}
