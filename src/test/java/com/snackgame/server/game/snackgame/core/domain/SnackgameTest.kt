package com.snackgame.server.game.snackgame.core.domain


import com.snackgame.server.game.snackgame.core.service.dto.StreakWithFever
import com.snackgame.server.game.snackgame.fixture.BoardFixture
import com.snackgame.server.game.snackgame.fixture.BoardFixture.TWO_BY_TWO_WITH_GOLDEN_SNACK
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

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

    @Test
    fun `피버타임_중에_발생한_스트릭은_점수가_2배가_된다`() {
        val game = Snackgame(땡칠().id, BoardFixture.TWO_BY_FOUR())
        game.startFeverTime()
        val streak = Streak.of(arrayListOf(Coordinate(0, 0), Coordinate(1, 0)))

        val now = LocalDateTime.now()
        val request = StreakWithFever(streak, clientIsFever = true, occurredAt = now)

        game.remove(request)

        assertThat(game.score).isEqualTo(4)
    }

    @Test
    fun `피버타임이_아닐_때_발생한_스트릭은_클라이언트가_우겨도_점수가_2배가_되지_않는다`() {
        val game = Snackgame(땡칠().id, BoardFixture.TWO_BY_FOUR())
        game.startFeverTime()
        val streak = Streak.of(arrayListOf(Coordinate(0, 0), Coordinate(1, 0)))

        val past = LocalDateTime.now().minusSeconds(10)
        val request = StreakWithFever(streak, clientIsFever = true, occurredAt = past)

        game.remove(request)

        assertThat(game.score).isEqualTo(2)
    }

    @Test
    fun `네트워크_지연으로_요청이_늦게_와도_발생_시각이_피버타임_내라면_2배_적용된다`() {
        val game = Snackgame(땡칠().id, BoardFixture.TWO_BY_FOUR())

        game.startFeverTime()


        val streak = Streak.of(arrayListOf(Coordinate(0, 0), Coordinate(1, 0)))
        val occurredAt = LocalDateTime.now().plusSeconds(5)

        val request = StreakWithFever(streak, clientIsFever = true, occurredAt = occurredAt)

        game.remove(request)

        assertThat(game.score).isEqualTo(4)
    }

    @Test
    fun `일시정지_후_재개하면_피버타임도_연장되어_점수_2배가_적용된다`() {
        val game = Snackgame(땡칠().id, BoardFixture.TWO_BY_FOUR())
        val feverStartTime = LocalDateTime.now()
        game.startFeverTime()

        // 피버타임 10초 후 일시정지
        val pauseAt = feverStartTime.plusSeconds(10)
        game.feverTime!!.pause(pauseAt)

        // 20초 일시정지 후 재개 (총 30초 경과, 하지만 피버는 10초만 흘렀음)
        val resumeAt = pauseAt.plusSeconds(20)
        game.feverTime!!.resume(resumeAt)

        val streak = Streak.of(arrayListOf(Coordinate(0, 0), Coordinate(1, 0)))

        // 재개 후 5초 뒤 (피버타임 15초 시점, 아직 30초 내)
        // 서버 시간 기준으로는 현재 시간 + 약간의 오차로 설정
        val occurredAt = LocalDateTime.now().plusSeconds(2)
        val request = StreakWithFever(streak, clientIsFever = true, occurredAt = occurredAt)

        game.remove(request)

        assertThat(game.score).isEqualTo(4)
    }
}
