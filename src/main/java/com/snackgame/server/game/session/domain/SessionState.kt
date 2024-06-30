package com.snackgame.server.game.session.domain

import com.snackgame.server.game.session.domain.SessionStateType.EXPIRED
import com.snackgame.server.game.session.domain.SessionStateType.IN_PROGRESS
import com.snackgame.server.game.session.domain.SessionStateType.PAUSED
import com.snackgame.server.game.session.exception.SessionNotPausedException
import com.snackgame.server.game.session.exception.SessionExpiredException
import com.snackgame.server.game.session.exception.SessionNotInProgressException
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import javax.persistence.Embeddable

@Embeddable
class SessionState(private val timeLimit: Duration) {

    var startedAt: LocalDateTime = now()
        private set
    private var pausedAt: LocalDateTime? = null
    var expiresAt: LocalDateTime = startedAt + timeLimit
        private set

    val current: SessionStateType
        get() {
            if (now().isAfter(expiresAt)) {
                return EXPIRED
            }
            if (pausedAt != null) {
                return PAUSED
            }
            return IN_PROGRESS
        }

    fun pause() {
        validateInProgress()
        with(now()) {
            pausedAt = this
            expiresAt = this + TTL
        }
    }

    fun resume() {
        validatePaused()
        val pausedDuration = Duration.between(pausedAt, now())
        startedAt += pausedDuration
        expiresAt = startedAt + timeLimit
        pausedAt = null
    }

    fun end() {
        validateNotExpired()
        if (pausedAt != null) {
            resume()
        }
        expiresAt = now().minusNanos(1)
    }

    private fun validateNotExpired() {
        if (current == EXPIRED) {
            throw SessionExpiredException()
        }
    }

    private fun validatePaused() {
        if (current != PAUSED) {
            throw SessionNotPausedException()
        }
    }

    fun validateInProgress() {
        if (current != IN_PROGRESS) {
            throw SessionNotInProgressException()
        }
    }

    companion object {
        val TTL: Duration = Duration.ofDays(7)
    }
}
