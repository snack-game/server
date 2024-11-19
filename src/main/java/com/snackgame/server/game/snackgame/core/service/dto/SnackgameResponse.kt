package com.snackgame.server.game.snackgame.core.service.dto

import com.snackgame.server.game.session.domain.Session
import com.snackgame.server.game.session.service.dto.SessionResponse
import com.snackgame.server.game.snackgame.biz.domain.SnackgameBiz
import com.snackgame.server.game.snackgame.biz.domain.SnackgameBizV2
import com.snackgame.server.game.snackgame.core.domain.Snackgame
import com.snackgame.server.game.snackgame.infinite.domain.SnackgameInfinite


class SnackgameResponse(
    session: Session,
    val board: List<List<SnackResponse>>
) : SessionResponse(session) {

    companion object {
        fun of(snackgame: Snackgame): SnackgameResponse {
            return SnackgameResponse(
                snackgame,
                SnackResponse.of(snackgame.board.getSnacks())
            )
        }

        fun of(snackgame: SnackgameBiz): SnackgameResponse {
            return SnackgameResponse(
                snackgame,
                SnackResponse.of(snackgame.board.getSnacks())
            )
        }

        fun of(snackgame: SnackgameBizV2): SnackgameResponse {
            return SnackgameResponse(
                snackgame,
                SnackResponse.of(snackgame.board.getSnacks())
            )
        }

        fun of(snackgame: SnackgameInfinite): SnackgameResponse {
            return SnackgameResponse(
                snackgame,
                listOf() // TODO: bring Board in
            )
        }
    }
}
