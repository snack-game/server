@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.game.snackgame.biz.domain

import com.snackgame.server.game.snackgame.domain.Coordinate
import com.snackgame.server.game.snackgame.domain.Streak
import com.snackgame.server.game.snackgame.fixture.TestFixture.THREE_BY_FOUR
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class SnackgameBizTest {

    @ParameterizedTest
    @MethodSource("길이가 각각 다른 스트릭들")
    fun `제거한 스트릭 길이만큼 점수를 얻는다`(coordinates: List<Coordinate>) {
        val game = SnackgameBiz(땡칠().id, THREE_BY_FOUR())

        val streak = Streak(coordinates)
        game.remove(streak)

        assertThat(game.score).isEqualTo(streak.length)
    }

    companion object {

        @JvmStatic
        fun `길이가 각각 다른 스트릭들`(): Stream<Arguments> = Stream.of(
            Arguments.of(
                listOf(
                    Coordinate(1, 0),
                    Coordinate(0, 0)
                ),
                listOf(
                    Coordinate(2, 1),
                    Coordinate(1, 0),
                    Coordinate(0, 0)
                )
            )
        )
    }
}