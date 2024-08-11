package com.snackgame.server.game.snackgame.service.dto

import com.snackgame.server.game.session.domain.Session
import com.snackgame.server.game.session.service.dto.SessionResponse
import com.snackgame.server.game.snackgame.domain.Snackgame

class SnackgameResponse(
    session: Session,
    val board: List<List<SnackResponse>>
) : SessionResponse(session) {

    companion object {
        fun of(snackgame: Snackgame): SnackgameResponse {
            return SnackgameResponse(
                snackgame,
                SnackResponse.of(snackgame.getSnacks())
            )
        }
    }
}
