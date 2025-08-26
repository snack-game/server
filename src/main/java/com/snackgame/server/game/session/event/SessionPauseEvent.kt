package com.snackgame.server.game.session.event

import com.snackgame.server.game.session.domain.Session
import java.time.LocalDateTime

data class SessionPauseEvent(
    override val sessionId: Long,
    override val ownerId: Long,
    val occurredAt: LocalDateTime
) : SessionStateEvent {

    companion object {
        fun of(session: Session): SessionPauseEvent {
            return SessionPauseEvent(
                session.sessionId,
                session.ownerId,
                LocalDateTime.now()
            )
        }
    }
}