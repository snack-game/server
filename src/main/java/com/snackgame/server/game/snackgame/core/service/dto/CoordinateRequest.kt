package com.snackgame.server.game.snackgame.core.service.dto

import com.snackgame.server.game.snackgame.core.domain.Coordinate

data class CoordinateRequest(
    var y: Int,
    var x: Int
){
    fun toCoordinate(): Coordinate =
        Coordinate(y, x)
}
