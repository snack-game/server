package com.snackgame.server.game.session.domain

import com.snackgame.server.game.session.exception.ScoreCanOnlyBeIncreasedException
import java.time.Duration

abstract class Session(
    timeLimit: Duration = SessionStatus.TTL,
    score: Int = 0
) {
    private val sessionStatus = SessionStatus(timeLimit)
    val currentStatus: SessionStatusType
        get() = sessionStatus.current
    var score: Int = score
        set(value) {
            sessionStatus.validateInProgress()
            if (value <= field) {
                throw ScoreCanOnlyBeIncreasedException()
            }
            field = value
        }

    fun pause() = sessionStatus.pause()
    fun resume() = sessionStatus.resume()
    fun end() = sessionStatus.end()
}

