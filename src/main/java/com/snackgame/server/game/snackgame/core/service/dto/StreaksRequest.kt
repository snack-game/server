package com.snackgame.server.game.snackgame.core.service.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.snackgame.server.game.snackgame.core.domain.Coordinate
import com.snackgame.server.game.snackgame.core.domain.Streak
import com.snackgame.server.game.snackgame.exception.InvalidStreakTimeException
import java.time.Duration
import java.time.LocalDateTime

data class StreaksRequest @JsonCreator constructor(
    val streaks: List<StreakWithMeta>
) {
    fun toStreaks(now: LocalDateTime = LocalDateTime.now()): List<StreakWithFever> =
        streaks.map { it.toDomain(now) }
}

data class StreakWithMeta(
    val coordinates: List<CoordinateRequest>,
    val isFever: Boolean,
    val occurredAt: LocalDateTime,
) {
    fun toDomain(now: LocalDateTime): StreakWithFever {
        if (Duration.between(occurredAt, now).abs() > REQUEST_TIME_TOLERANCE) {
            throw InvalidStreakTimeException()
        }

        return StreakWithFever(
            streak = Streak.of(coordinates.map { Coordinate(it.y, it.x) }),
            clientIsFever = isFever,
            occurredAt = occurredAt
        )
    }

    companion object {
        private val REQUEST_TIME_TOLERANCE: Duration = Duration.ofSeconds(2)
    }
}

data class StreakWithFever(
    val streak: Streak,
    val clientIsFever: Boolean,
    val occurredAt: LocalDateTime
)