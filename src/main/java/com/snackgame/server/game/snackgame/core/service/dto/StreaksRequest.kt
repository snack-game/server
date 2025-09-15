package com.snackgame.server.game.snackgame.core.service.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.snackgame.server.game.snackgame.core.domain.Coordinate
import com.snackgame.server.game.snackgame.core.domain.Streak
import java.time.LocalDateTime

data class StreaksRequest @JsonCreator constructor(
    val streaks: List<StreakWithMeta>
) {
    fun toStreaks(): List<StreakWithFever> =
        streaks.map { it.toDomain() }
}

data class StreakWithMeta(
    val coordinates: List<CoordinateRequest>,
    val isFever: Boolean,
    val occurredAt: LocalDateTime,
) {
    fun toDomain(): StreakWithFever {
        return StreakWithFever(
            streak = Streak.of(coordinates.map { Coordinate(it.y, it.x) }),
            clientIsFever = isFever,
            occurredAt = occurredAt
        )
    }
}

data class StreakWithFever(
    val streak: Streak,
    val clientIsFever: Boolean,
    val occurredAt: LocalDateTime
)