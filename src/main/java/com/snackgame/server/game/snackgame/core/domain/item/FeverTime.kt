package com.snackgame.server.game.snackgame.core.domain.item

import java.time.Duration
import java.time.LocalDateTime
import javax.persistence.Embeddable

@Embeddable
class FeverTime(
    private val feverStartedAt: LocalDateTime? = null
) {
    fun isActive(now: LocalDateTime = LocalDateTime.now()): Boolean {
        return feverStartedAt != null && Duration.between(feverStartedAt, now) < DURATION
    }

    companion object {
        private val DURATION: Duration = Duration.ofSeconds(30)

        fun start(now: LocalDateTime = LocalDateTime.now()): FeverTime {
            return FeverTime(now)
        }
    }
}