package com.snackgame.server.game.snackgame.service.dto

import com.snackgame.server.game.snackgame.domain.Coordinate
import com.snackgame.server.game.snackgame.domain.Streak

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
