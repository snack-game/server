package com.snackgame.server.game.session.domain

import com.snackgame.server.game.session.exception.SessionTimeAlreadyPausedException
import com.snackgame.server.game.session.exception.SessionTimeAlreadyTickingException
import com.snackgame.server.game.session.exception.SessionTimeExpiredException
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalDateTime.now

class SessionTime(private val limit: Duration) {

    var startedAt: LocalDateTime = now()
        private set
    private var pausedAt: LocalDateTime? = null
        private set
    var expiresAt: LocalDateTime = startedAt + limit
        private set

    val isExpired get() = now().isAfter(expiresAt)

    fun pause() {
        validateNotExpired()
        validateTicking()
        with(now()) {
            pausedAt = this
            expiresAt = this + TTL
        }
    }

    fun resume() {
        validateNotExpired()
        validatePaused()
        val pausedDuration = Duration.between(pausedAt, now())
        startedAt += pausedDuration
        expiresAt = startedAt + limit
        pausedAt = null
    }

    fun end() {
        validateNotExpired()
        if (pausedAt != null) {
            resume()
        }
        expiresAt = now()
    }

    private fun validateNotExpired() {
        if (isExpired) {
            throw SessionTimeExpiredException()
        }
    }

    private fun validatePaused() {
        if (pausedAt == null) {
            throw SessionTimeAlreadyTickingException()
        }
    }

    private fun validateTicking() {
        if (pausedAt != null) {
            throw SessionTimeAlreadyPausedException()
        }
    }

    companion object {
        val TTL: Duration = Duration.ofDays(7)
    }
}
