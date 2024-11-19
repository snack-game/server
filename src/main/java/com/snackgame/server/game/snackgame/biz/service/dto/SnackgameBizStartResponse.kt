package com.snackgame.server.game.snackgame.biz.service.dto

import com.snackgame.server.game.metadata.MetadataResponse
import com.snackgame.server.game.session.domain.SessionStateType
import com.snackgame.server.game.snackgame.core.service.dto.SnackResponse
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameResponse
import java.time.LocalDateTime

data class SnackgameBizStartResponse(
    val metadata: MetadataResponse,
    val ownerId: Long,
    val sessionId: Long,
    val state: SessionStateType,
    val score: Int,
    val createdAt: LocalDateTime,
    val board: List<List<SnackResponse>>,
    val token: String
) {

    companion object {

        fun of(snackgame: SnackgameResponse, token: String): SnackgameBizStartResponse {
            return SnackgameBizStartResponse(
                snackgame.metadata,
                snackgame.ownerId,
                snackgame.sessionId,
                snackgame.state,
                snackgame.score,
                snackgame.createdAt,
                snackgame.board,
                token
            )
        }
    }
}

