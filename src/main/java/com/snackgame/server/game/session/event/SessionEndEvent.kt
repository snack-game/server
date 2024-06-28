package com.snackgame.server.game.session.event

import com.snackgame.server.game.session.domain.Session

data class SessionEndEvent(
    val gameId: Long,
    val ownerId: Long,
    val sessionId: Long,
    val score: Int
) {

    companion object {
        fun of(session: Session): SessionEndEvent {
            return SessionEndEvent(
                2, // TODO: Session에 abstractional gameId를 생성
                session.ownerId,
                session.sessionId,
                session.score
            )
        }
    }
}

