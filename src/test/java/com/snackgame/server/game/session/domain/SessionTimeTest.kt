@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.game.session.domain

import com.snackgame.server.game.session.exception.SessionTimeAlreadyPausedException
import com.snackgame.server.game.session.exception.SessionTimeAlreadyTickingException
import com.snackgame.server.game.session.exception.SessionTimeException
import org.assertj.core.api.AbstractLocalDateTimeAssert
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class SessionTimeTest {

    @Test
    fun `일시정지하면 7일후 자동으로 만료된다`() {
        val sessionTime = SessionTime(SESSION_TIME_LIMIT)

        sessionTime.pause()

        assertThat(sessionTime.expiresAt).isCloseTo(sessionTime.startedAt + Duration.ofDays(7))
    }

    @Test
    fun `경과중인 경우에만 일시정지 할 수 있다`() {
        val sessionTime = SessionTime(SESSION_TIME_LIMIT)
        sessionTime.pause()

        assertThatThrownBy { sessionTime.pause() }
            .isInstanceOf(SessionTimeAlreadyPausedException::class.java)
    }

    @Test
    fun `이미 경과중인 경우 재개할 수 없다`() {
        val sessionTime = SessionTime(SESSION_TIME_LIMIT)

        assertThatThrownBy { sessionTime.resume() }
            .isInstanceOf(SessionTimeAlreadyTickingException::class.java)
    }

    @Test
    fun `재개하면 시작 시각이 일시정지한 시간만큼 밀려난다`() {
        val tickDuration = Duration.ofSeconds(2)
        val pauseDuration = Duration.ofSeconds(1)
        val sessionTime = SessionTime(SESSION_TIME_LIMIT)
        val sessionTimeStartedAt = sessionTime.startedAt

        sessionTime.tick(tickDuration, pauseDuration)

        assertThat(sessionTime.startedAt).isCloseTo(sessionTimeStartedAt + pauseDuration)
    }

    @Test
    fun `재개하면 종료 시각도 일시정지한 시간만큼 밀려난다`() {
        val tickDuration = Duration.ofSeconds(2)
        val pauseDuration = Duration.ofSeconds(1)
        val sessionTime = SessionTime(SESSION_TIME_LIMIT)
        val sessionTimeStartedAt = sessionTime.startedAt

        sessionTime.tick(tickDuration, pauseDuration)

        assertThat(sessionTime.expiresAt).isCloseTo(sessionTimeStartedAt + SESSION_TIME_LIMIT + pauseDuration)
    }

    @Test
    fun `일시정지 중에도 종료할 수 있다`() {
        val sessionTime = SessionTime(SESSION_TIME_LIMIT)
        sessionTime.pause()

        sessionTime.end()

        assertThat(sessionTime.isExpired).isTrue()
    }

    @Test
    fun `만료된 세션시간은 조작할 수 없다`() {
        val sessionTime = SessionTime(Duration.ZERO)

        val operations = listOf(sessionTime::pause, sessionTime::resume, sessionTime::end)
        operations.forEach {
            assertThatThrownBy { it.invoke() }
                .isInstanceOf(SessionTimeException::class.java)
        }
    }

    companion object {
        private val SESSION_TIME_LIMIT = Duration.ofMinutes(2)
    }
}

private fun SessionTime.tick(tickDuration: Duration, pauseDuration: Duration): SessionTime {
    Thread.sleep(tickDuration.toMillis())

    this.pause()
    Thread.sleep(pauseDuration.toMillis())

    this.resume()
    return this
}

private fun <SELF : AbstractLocalDateTimeAssert<SELF>?> AbstractLocalDateTimeAssert<SELF>.isCloseTo(other: LocalDateTime): SELF {
    return this.isCloseTo(other, within(10, ChronoUnit.MILLIS))
}
