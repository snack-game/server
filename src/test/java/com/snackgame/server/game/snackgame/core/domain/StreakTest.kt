@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.game.snackgame.core.domain

import com.snackgame.server.game.snackgame.exception.InvalidStreakException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatNoException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class StreakTest {

    @Test
    fun `스트릭의 길이는 2 이상이어야 한다`() {
        assertThatThrownBy { Streak.of(listOf(CENTER)) }
            .isInstanceOf(InvalidStreakException::class.java)
    }

    @Test
    fun `폭탄 스트릭의 길이는 2 이상이어야 한다`() {
        assertThatThrownBy { Streak.bomb(listOf(CENTER)) }
            .isInstanceOf(InvalidStreakException::class.java)
    }

    @ParameterizedTest
    @MethodSource("거리가 길이가 다른 좌표들")
    fun `스트릭 길이를 알 수 있다`(coordinates: List<Coordinate>, length: Int) {
        assertThat(Streak.of(coordinates).length).isEqualTo(length)
    }

    @Test
    fun `동일한 좌표가 여러번 포함될 수 없다`() {
        assertThatThrownBy {
            Streak.of(
                listOf(
                    Coordinate(1, 1),
                    Coordinate(1, 2),
                    Coordinate(1, 1)
                )
            )
        }
            .isInstanceOf(InvalidStreakException::class.java)
    }

    @Test
    fun `폭탄 스트릭에 동일한 좌표가 여러번 포함될 수 없다`() {
        assertThatThrownBy {
            Streak.bomb(
                listOf(
                    Coordinate(1, 1),
                    Coordinate(1, 2),
                    Coordinate(1, 1)
                )
            )
        }
            .isInstanceOf(InvalidStreakException::class.java)
    }

    @ParameterizedTest
    @MethodSource("상하좌우 방향 좌표")
    fun `스트릭은 상하좌우 방향의 좌표들로 이어져야 한다`(other: Coordinate) {
        assertThatNoException().isThrownBy { Streak.of(listOf(CENTER, other)) }
    }

    @ParameterizedTest
    @MethodSource("대각선 방향 좌표")
    fun `스트릭은 대각선 방향의 좌표들로 이어져야 한다`(other: Coordinate) {
        assertThatNoException().isThrownBy { Streak.of(listOf(CENTER, other)) }
    }

    @ParameterizedTest
    @MethodSource("상하좌우 및 대각선 외의 좌표")
    fun `상하좌우나 대각선 방향으로 이어지지 않는 좌표들로 스트릭을 만들 수 없다`(other: Coordinate) {
        assertThatThrownBy { Streak.of(listOf(CENTER, other)) }
            .isInstanceOf(InvalidStreakException::class.java)
    }

    companion object {
        private val CENTER = Coordinate(2, 2)

        @JvmStatic
        fun `거리가 길이가 다른 좌표들`(): Stream<Arguments> = Stream.of(
            Arguments.of(
                listOf(
                    CENTER,
                    Coordinate(1, 1),
                    Coordinate(1, 2)
                ),
                3
            ),
            Arguments.of(
                listOf(
                    CENTER,
                    Coordinate(1, 1),
                    Coordinate(1, 10)
                ),
                3
            )
        )

        @JvmStatic
        fun `상하좌우 방향 좌표`(): Stream<Arguments> = Stream.of(
            Arguments.of(Coordinate(2, 1)),
            Arguments.of(Coordinate(3, 2)),
            Arguments.of(Coordinate(2, 3)),
            Arguments.of(Coordinate(1, 2))
        )

        @JvmStatic
        fun `대각선 방향 좌표`(): Stream<Arguments> = Stream.of(
            Arguments.of(Coordinate(1, 1)),
            Arguments.of(Coordinate(3, 1)),
            Arguments.of(Coordinate(3, 3)),
            Arguments.of(Coordinate(1, 3))
        )

        @JvmStatic
        fun `상하좌우 및 대각선 외의 좌표`(): Stream<Arguments> = Stream.of(
            Arguments.of(Coordinate(0, 1)),
            Arguments.of(Coordinate(1, 0)),
            Arguments.of(Coordinate(3, 0)),
            Arguments.of(Coordinate(4, 1)),
            Arguments.of(Coordinate(4, 3)),
            Arguments.of(Coordinate(3, 4)),
            Arguments.of(Coordinate(1, 4)),
            Arguments.of(Coordinate(0, 3))
        )
    }
}
