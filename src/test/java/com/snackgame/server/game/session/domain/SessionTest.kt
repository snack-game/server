package com.snackgame.server.game.session.domain

import com.snackgame.server.game.session.domain.SessionStateType.*
import com.snackgame.server.game.session.exception.ScoreCanOnlyBeIncreasedException
import com.snackgame.server.game.session.exception.SessionNotInProgressException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@Suppress("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
class SessionTest {

    private class SomeSession : Session(0)


    @Test
    fun `세션 주인을 저장한다`() {
        assertThat(Session::class.java).hasDeclaredFields("ownerId")
    }

    @Test
    fun `점수는 기본 0부터 시작한다`() {
        val someSession: Session = SomeSession()

        assertThat(someSession.score).isZero()
    }

    @Test
    fun `점수를 증가시킬 수 있다`() {
        val someSession: Session = SomeSession()

        someSession.score += 1

        assertThat(someSession.score).isOne()
    }

    @Test
    fun `(아직) 점수를 감소시킬 수는 없다`() {
        val someExpiredSession = SomeSession()

        assertThatThrownBy { someExpiredSession.score -= 1 }
            .isInstanceOf(ScoreCanOnlyBeIncreasedException::class.java)
    }

    @Nested
    inner class 세션을 {
        @Test
        fun `일시정지할 수 있다`() {
            val someSession = SomeSession()

            someSession.pause()

            assertThat(someSession.currentState).isEqualTo(PAUSED)
        }

        @Test
        fun `재개할 수 있다`() {
            val somePausedSession = SomeSession().also { it.pause() }

            somePausedSession.resume()

            assertThat(somePausedSession.currentState).isEqualTo(IN_PROGRESS)
        }

        @Test
        fun `종료할 수 있다`() {
            val someSession = SomeSession()

            someSession.end()

            assertThat(someSession.currentState).isEqualTo(EXPIRED)
        }
    }

    @Test
    fun `만료된 세션은 변경할 수 없다`() {
        val someExpiredSession = SomeSession().also { it.end() }

        assertThatThrownBy { someExpiredSession.score += 1 }
            .isInstanceOf(SessionNotInProgressException::class.java)
    }
}
