package com.snackgame.server.game.session.event

import com.snackgame.server.game.metadata.Metadata
import com.snackgame.server.game.session.domain.Session

data class SessionEndEvent(
    val metadata: Metadata,
    val ownerId: Long,
    val sessionId: Long,
    val score: Int
) {

    companion object {
        fun of(session: Session): SessionEndEvent {
            return SessionEndEvent(
                session.metadata,
                session.ownerId,
                session.sessionId,
                session.score
            )
        }
    }
}

