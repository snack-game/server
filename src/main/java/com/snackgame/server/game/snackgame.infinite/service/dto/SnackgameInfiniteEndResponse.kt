package com.snackgame.server.game.snackgame.service.dto


import com.snackgame.server.game.session.domain.Session
import com.snackgame.server.game.session.service.dto.SessionResponse
import com.snackgame.server.game.snackgame.domain.Percentile
import com.snackgame.server.game.snackgame.domain.SnackgameInfinite
import io.swagger.v3.oas.annotations.media.Schema

class SnackgameInfiniteEndResponse(
    session: Session,
    @field:Schema(example = "20")
    val percentile: Int
) : SessionResponse(session) {

    companion object {
        fun of(snackgame: SnackgameInfinite, percentile: Percentile): SnackgameInfiniteEndResponse {
            return SnackgameInfiniteEndResponse(
                snackgame,
                percentile.percentage()
            )
        }
    }
}
