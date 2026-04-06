package com.snackgame.server.game.snackgame.core.domain.item

import java.time.Duration
import java.time.LocalDateTime
import javax.persistence.Embeddable

@Embeddable
class FeverTime(
    val feverStartedAt: LocalDateTime,
    var feverEndAt: LocalDateTime,
    var lastPausedAt: LocalDateTime? = null,
    var paused: Boolean? = false,
    var feverStreakCount: Int? = 0
) {

    fun isFeverTime(occurredAt: LocalDateTime, serverNow: LocalDateTime = LocalDateTime.now()): Boolean {
        // 1. 서버 시간 기준 합리성 검증 (치팅 방지)
        if (occurredAt.isBefore(serverNow.minus(MAX_PAST_ALLOWED)) ||
            occurredAt.isAfter(serverNow.plus(MAX_FUTURE_ALLOWED))
        ) {
            return false
        }

        // 2. 피버타임 범위 검증 (서버 연산 및 네트워크 지연 고려)
        val validEndAt = feverEndAt.plus(BUFFER_DURATION)
        val validStartedAt = feverStartedAt.minus(BUFFER_DURATION)

        if (occurredAt.isBefore(validStartedAt) || occurredAt.isAfter(validEndAt)) {
            return false
        }
        return true
    }

    fun canApplyFeverMultiplier(): Boolean = (feverStreakCount ?: 0) < MAX_FEVER_STREAKS

    fun incrementFeverStreak() {
        feverStreakCount = (feverStreakCount ?: 0) + 1
    }

    fun pause(at: LocalDateTime) {
        if (paused != true) {
            this.paused = true
            this.lastPausedAt = at
        }
    }

    fun resume(at: LocalDateTime) {
        if (paused == true && lastPausedAt != null) {
            val pausedDuration = Duration.between(lastPausedAt, at)

            this.feverEndAt = this.feverEndAt.plus(pausedDuration)

            this.paused = false
            this.lastPausedAt = null
        }
    }

    companion object {
        private val FEVER_DURATION: Duration = Duration.ofSeconds(30)
        private val BUFFER_DURATION: Duration = Duration.ofSeconds(1)
        private val MAX_PAST_ALLOWED: Duration = Duration.ofMinutes(3)
        private val MAX_FUTURE_ALLOWED: Duration = Duration.ofSeconds(5)
        const val MAX_FEVER_STREAKS = 60

        fun start(now: LocalDateTime = LocalDateTime.now()): FeverTime {
            return FeverTime(
                feverStartedAt = now,
                feverEndAt = now.plus(FEVER_DURATION),
                paused = false
            )
        }
    }
}