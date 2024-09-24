package com.snackgame.server.game.snackgame.core.service.dto

import com.snackgame.server.game.snackgame.core.domain.Coordinate
import com.snackgame.server.game.snackgame.core.domain.Streak

data class StreakRequest(
    var y: Int,
    var x: Int
) {
    companion object {
        fun toStreak(requests: List<StreakRequest>): Streak {
            val coordinates = requests.map { Coordinate(it.y, it.x) }.toMutableList()
            return Streak(coordinates)
        }
    }
}
