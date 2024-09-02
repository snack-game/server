package com.snackgame.server.game.snackgame.service.dto


import com.snackgame.server.game.session.domain.Session
import com.snackgame.server.game.session.service.dto.SessionResponse
import com.snackgame.server.game.snackgame.domain.Percentile
import com.snackgame.server.game.snackgame.domain.Snackgame
import io.swagger.v3.oas.annotations.media.Schema

class SnackgameEndResponse(
    session: Session,
    @field:Schema(example = "20")
    val percentile: Int
) : SessionResponse(session) {

    companion object {
        fun of(snackgame: Snackgame, percentile: Percentile): SnackgameEndResponse {
            return SnackgameEndResponse(
                snackgame,
                percentile.percentage()
            )
        }
    }
}
