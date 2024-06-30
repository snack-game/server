@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.game.session.domain

import com.snackgame.server.game.session.domain.SessionStateType.EXPIRED
import com.snackgame.server.game.session.exception.SessionNotPausedException
import com.snackgame.server.game.session.exception.SessionNotInProgressException
import org.assertj.core.api.AbstractLocalDateTimeAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class SessionStateTest {

    @Test
    fun `일시정지하면 7일후 자동으로 만료된다`() {
        val sessionState = SessionState(SESSION_TIME_LIMIT)

        sessionState.pause()

        assertThat(sessionState.expiresAt).isCloseTo(sessionState.startedAt + Duration.ofDays(7))
    }

    @Test
    fun `진행중인 경우에만 일시정지 할 수 있다`() {
        val pausedSessionState = SessionState(SESSION_TIME_LIMIT).also { it.pause() }
        val expiredSessionState = SessionState(Duration.ZERO)

        assertSoftly {
            with(it) {
                assertThatThrownBy { pausedSessionState.pause() }
                    .isInstanceOf(SessionNotInProgressException::class.java)
                assertThatThrownBy { expiredSessionState.pause() }
                    .isInstanceOf(SessionNotInProgressException::class.java)
            }
        }
    }

    @Test
    fun `일시중지된 경우에만 재개할 수 있다`() {
        val sessionStateInProgress = SessionState(SESSION_TIME_LIMIT)
        val expiredSessionState = SessionState(Duration.ZERO)

        assertSoftly {
            with(it) {
                assertThatThrownBy { sessionStateInProgress.resume() }
                    .isInstanceOf(SessionNotPausedException::class.java)
                assertThatThrownBy { expiredSessionState.resume() }
                    .isInstanceOf(SessionNotPausedException::class.java)
            }
        }
    }

    @Test
    fun `재개하면 시작 시각이 일시정지했던 시간만큼 밀려난다`() {
        val proceedDuration = Duration.ofSeconds(2)
        val pauseDuration = Duration.ofSeconds(1)
        val sessionState = SessionState(SESSION_TIME_LIMIT)
        val firstlyStartedAt = sessionState.startedAt

        sessionState.proceed(proceedDuration, pauseDuration)

        assertThat(sessionState.startedAt).isCloseTo(firstlyStartedAt + pauseDuration)
    }

    @Test
    fun `재개하면 종료 시각도 일시정지했던 시간만큼 밀려난다`() {
        val proceedDuration = Duration.ofSeconds(2)
        val pauseDuration = Duration.ofSeconds(1)
        val sessionState = SessionState(SESSION_TIME_LIMIT)
        val firstlyStartedAt = sessionState.startedAt

        sessionState.proceed(proceedDuration, pauseDuration)

        assertThat(sessionState.expiresAt).isCloseTo(firstlyStartedAt + SESSION_TIME_LIMIT + pauseDuration)
    }

    @Test
    fun `일시정지 중에도 종료할 수 있다`() {
        val pausedSessionState = SessionState(SESSION_TIME_LIMIT).also { it.pause() }

        pausedSessionState.end()

        assertThat(pausedSessionState.current).isEqualTo(EXPIRED)
    }

    @Test
    fun `만료된 세션시간은 조작할 수 없다`() {
        val expiredSessionState = SessionState(Duration.ZERO)

        assertSoftly {
            with(it) {
                assertThatThrownBy { expiredSessionState.pause() }
                assertThatThrownBy { expiredSessionState.resume() }
                assertThatThrownBy { expiredSessionState.end() }
            }
        }
    }

    companion object {
        private val SESSION_TIME_LIMIT = Duration.ofMinutes(2)
    }
}

private fun SessionState.proceed(proceedDuration: Duration, pauseDuration: Duration): SessionState {
    Thread.sleep(proceedDuration.toMillis())

    this.pause()
    Thread.sleep(pauseDuration.toMillis())

    this.resume()
    return this
}

private fun <SELF : AbstractLocalDateTimeAssert<SELF>?> AbstractLocalDateTimeAssert<SELF>.isCloseTo(other: LocalDateTime): SELF {
    return this.isCloseTo(other, within(10, ChronoUnit.MILLIS))
}
