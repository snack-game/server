package com.snackgame.server.game.session.event

import com.snackgame.server.game.session.domain.Session
import java.time.LocalDateTime

data class SessionResumeEvent(
    override val sessionId: Long,
    override val ownerId: Long,
    val occurredAt: LocalDateTime
) : SessionStateEvent {

    companion object {
        fun of(session: Session): SessionResumeEvent {
            return SessionResumeEvent(
                session.sessionId,
                session.ownerId,
                LocalDateTime.now()
            )
        }
    }
}
