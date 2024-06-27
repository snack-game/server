package com.snackgame.server.game.session.service.dto

import com.snackgame.server.game.session.domain.Session
import com.snackgame.server.game.session.domain.SessionStateType
import java.io.Serializable
import java.time.LocalDateTime

abstract class SessionResponse(
    session: Session
) : Serializable {

    val ownerId: Long = session.ownerId
    val sessionId: Long = session.sessionId
    val state: SessionStateType = session.currentState
    val score: Int = session.score
    val createdAt: LocalDateTime = session.createdAt
}