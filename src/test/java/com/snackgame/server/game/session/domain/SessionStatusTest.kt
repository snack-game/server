@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.game.session.domain

import com.snackgame.server.game.session.domain.SessionStatusType.EXPIRED
import com.snackgame.server.game.session.exception.SessionAlreadyInProgressException
import com.snackgame.server.game.session.exception.SessionNotInProgressException
import org.assertj.core.api.AbstractLocalDateTimeAssert
import org.assertj.core.api.Assertions.*
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class SessionStatusTest {

    @Test
    fun `일시정지하면 7일후 자동으로 만료된다`() {
        val sessionStatus = SessionStatus(SESSION_TIME_LIMIT)

        sessionStatus.pause()

        assertThat(sessionStatus.expiresAt).isCloseTo(sessionStatus.startedAt + Duration.ofDays(7))
    }

    @Test
    fun `진행중인 경우에만 일시정지 할 수 있다`() {
        val pausedSessionStatus = SessionStatus(SESSION_TIME_LIMIT).also { it.pause() }
        val expiredSessionStatus = SessionStatus(Duration.ZERO)

        assertSoftly {
            with(it) {
                assertThatThrownBy { pausedSessionStatus.pause() }
                    .isInstanceOf(SessionNotInProgressException::class.java)
                assertThatThrownBy { expiredSessionStatus.pause() }
                    .isInstanceOf(SessionNotInProgressException::class.java)
            }
        }
    }

    @Test
    fun `이미 진행중인 경우 재개할 수 없다`() {
        val sessionStatus = SessionStatus(SESSION_TIME_LIMIT)

        assertThatThrownBy { sessionStatus.resume() }
            .isInstanceOf(SessionAlreadyInProgressException::class.java)
    }

    @Test
    fun `재개하면 시작 시각이 일시정지했던 시간만큼 밀려난다`() {
        val proceedDuration = Duration.ofSeconds(2)
        val pauseDuration = Duration.ofSeconds(1)
        val sessionStatus = SessionStatus(SESSION_TIME_LIMIT)
        val firstlyStartedAt = sessionStatus.startedAt

        sessionStatus.proceed(proceedDuration, pauseDuration)

        assertThat(sessionStatus.startedAt).isCloseTo(firstlyStartedAt + pauseDuration)
    }

    @Test
    fun `재개하면 종료 시각도 일시정지했던 시간만큼 밀려난다`() {
        val proceedDuration = Duration.ofSeconds(2)
        val pauseDuration = Duration.ofSeconds(1)
        val sessionStatus = SessionStatus(SESSION_TIME_LIMIT)
        val firstlyStartedAt = sessionStatus.startedAt

        sessionStatus.proceed(proceedDuration, pauseDuration)

        assertThat(sessionStatus.expiresAt).isCloseTo(firstlyStartedAt + SESSION_TIME_LIMIT + pauseDuration)
    }

    @Test
    fun `일시정지 중에도 종료할 수 있다`() {
        val pausedSessionStatus = SessionStatus(SESSION_TIME_LIMIT).also { it.pause() }

        pausedSessionStatus.end()

        assertThat(pausedSessionStatus.current).isEqualTo(EXPIRED)
    }

    @Test
    fun `만료된 세션시간은 조작할 수 없다`() {
        val expiredSessionStatus = SessionStatus(Duration.ZERO)

        assertSoftly {
            with(it) {
                assertThatThrownBy { expiredSessionStatus.pause() }
                assertThatThrownBy { expiredSessionStatus.resume() }
                assertThatThrownBy { expiredSessionStatus.end() }
            }
        }
    }

    companion object {
        private val SESSION_TIME_LIMIT = Duration.ofMinutes(2)
    }
}

private fun SessionStatus.proceed(proceedDuration: Duration, pauseDuration: Duration): SessionStatus {
    Thread.sleep(proceedDuration.toMillis())

    this.pause()
    Thread.sleep(pauseDuration.toMillis())

    this.resume()
    return this
}

private fun <SELF : AbstractLocalDateTimeAssert<SELF>?> AbstractLocalDateTimeAssert<SELF>.isCloseTo(other: LocalDateTime): SELF {
    return this.isCloseTo(other, within(10, ChronoUnit.MILLIS))
}
