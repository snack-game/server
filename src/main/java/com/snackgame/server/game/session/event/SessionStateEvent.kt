package com.snackgame.server.game.session.event

interface SessionStateEvent {
    val sessionId: Long
    val ownerId: Long
}
