package com.snackgame.server.game.snackgame.core.domain.item

import java.time.Duration
import java.time.LocalDateTime
import javax.persistence.Embeddable

@Embeddable
class FeverTime(
    val feverStartedAt: LocalDateTime,
    var feverEndAt: LocalDateTime,
    var lastPausedAt: LocalDateTime? = null,
    var paused: Boolean? = false
) {

    fun isFeverTime(occurredAt: LocalDateTime): Boolean {
        val validStartedAt = feverStartedAt.minus(BUFFER_DURATION)

        if (occurredAt.isBefore(validStartedAt) || occurredAt.isAfter(feverEndAt)) {
            return false
        }
        return true
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
            // 쉬었던 만큼 종료 시간을 뒤로 미룸
            this.feverEndAt = this.feverEndAt.plus(pausedDuration)

            this.paused = false
            this.lastPausedAt = null
        }
    }

    companion object {
        private val FEVER_DURATION: Duration = Duration.ofSeconds(30)
        private val BUFFER_DURATION: Duration = Duration.ofSeconds(1)

        fun start(now: LocalDateTime = LocalDateTime.now()): FeverTime {
            return FeverTime(
                feverStartedAt = now,
                feverEndAt = now.plus(FEVER_DURATION),
                paused = false
            )
        }
    }
}