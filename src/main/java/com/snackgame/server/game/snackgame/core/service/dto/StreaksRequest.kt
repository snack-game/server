package com.snackgame.server.game.snackgame.core.service.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.snackgame.server.game.snackgame.core.domain.Coordinate
import com.snackgame.server.game.snackgame.core.domain.Streak

data class StreaksRequest @JsonCreator constructor(
    val streaks: List<List<CoordinateRequest>>
) {

    fun toStreaks(): List<Streak> = streaks.map { streak ->
        Streak(streak.map { Coordinate(it.y, it.x) })
    }
}
