package com.snackgame.server.game.session.service.dto

import com.snackgame.server.game.metadata.MetadataResponse
import com.snackgame.server.game.session.domain.Session
import java.io.Serializable

abstract class SessionResponse(
    session: Session
) : Serializable {

    val metadata = MetadataResponse.of(session.metadata)
    val ownerId = session.ownerId
    val sessionId = session.sessionId
    val state = session.currentState
    val score = session.score
    val createdAt = session.createdAt
}
