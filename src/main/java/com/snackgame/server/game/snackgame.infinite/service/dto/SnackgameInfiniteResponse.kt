package com.snackgame.server.game.snackgame.service.dto

import com.snackgame.server.game.session.domain.Session
import com.snackgame.server.game.session.service.dto.SessionResponse
import com.snackgame.server.game.snackgame.domain.SnackgameInfinite

class SnackgameInfiniteResponse(
    session: Session,
    val board: String = "" // TODO: replace with board
) : SessionResponse(session) {

    companion object {
        fun of(snackgame: SnackgameInfinite): SnackgameInfiniteResponse {
            return SnackgameInfiniteResponse(
                snackgame,
                "준비중"
            )
        }
    }
}
