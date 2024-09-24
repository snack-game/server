package com.snackgame.server.game.snackgame.core.service.dto


import com.snackgame.server.game.session.domain.Session
import com.snackgame.server.game.session.service.dto.SessionResponse
import com.snackgame.server.game.snackgame.biz.domain.SnackgameBiz
import com.snackgame.server.game.snackgame.core.domain.Percentile
import com.snackgame.server.game.snackgame.core.domain.Snackgame
import com.snackgame.server.game.snackgame.infinite.domain.SnackgameInfinite
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

        fun of(snackgame: SnackgameInfinite, percentile: Percentile): SnackgameEndResponse {
            return SnackgameEndResponse(
                snackgame,
                percentile.percentage()
            )
        }

        fun of(snackgame: SnackgameBiz, percentile: Percentile): SnackgameEndResponse {
            return SnackgameEndResponse(
                snackgame,
                percentile.percentage()
            )
        }
    }
}
