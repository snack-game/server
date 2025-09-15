package com.snackgame.server.game.snackgame.core.domain.item

import com.snackgame.server.game.snackgame.exception.InvalidStreakTimeException
import java.time.Duration
import java.time.LocalDateTime
import javax.persistence.Embeddable

@Embeddable
class FeverTime(
    private var feverStartedAt: LocalDateTime? = null,
    private var feverRemains: Duration,
    private var lastResumedAt: LocalDateTime? = null,
    private var paused: Boolean? = false,
) {

    fun isActive(at: LocalDateTime): Boolean {
        if (paused!! || feverRemains <= Duration.ZERO) return false

        val feverUsed = Duration.between(lastResumedAt, at)
        return (feverRemains - feverUsed) > Duration.ZERO
    }

    fun pause(at: LocalDateTime) {
        if (!paused!! && lastResumedAt != null) {
            val feverUsed = Duration.between(lastResumedAt, at)
            feverRemains = (feverRemains - feverUsed).coerceAtLeast(Duration.ZERO)
            paused = true
            lastResumedAt = null
        }
    }

    fun resume(at: LocalDateTime) {
        if (paused!! && feverRemains > Duration.ZERO) {
            paused = false
            lastResumedAt = at
        }
    }

    fun validateFeverStreakOccurredAt(streakOccurredAt: LocalDateTime): Boolean {
        val feverEndAt = calculateFeverEnd(streakOccurredAt)
        if (streakOccurredAt.isBefore(feverStartedAt) || streakOccurredAt.isAfter(feverEndAt))
            throw InvalidStreakTimeException()
        return true
    }

    private fun calculateFeverEnd(at: LocalDateTime): LocalDateTime {
        val progressed = (!paused!! && lastResumedAt != null)
            .takeIf { it }?.let { Duration.between(lastResumedAt, at) } ?: Duration.ZERO

        val remaining = (feverRemains - progressed).coerceAtLeast(Duration.ZERO)
        return at.minus(progressed).plus(remaining)
    }

    companion object {
        private val FEVER_TIME_PERIOD: Duration = Duration.ofSeconds(30)

        fun start(now: LocalDateTime = LocalDateTime.now()): FeverTime {
            return FeverTime(
                feverStartedAt = now,
                feverRemains = FEVER_TIME_PERIOD,
                lastResumedAt = now,
                paused = false
            )
        }
    }
}
