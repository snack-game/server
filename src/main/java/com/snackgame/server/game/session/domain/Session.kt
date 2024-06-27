package com.snackgame.server.game.session.domain

import com.snackgame.server.common.domain.BaseEntity
import com.snackgame.server.game.session.exception.ScoreCanOnlyBeIncreasedException
import java.time.Duration
import javax.persistence.Embedded
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class Session(
    val ownerId: Long,
    timeLimit: Duration = SessionState.TTL,
    score: Int = 0,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val sessionId: Long = 0
) : BaseEntity() {
    @Embedded
    private val sessionState = SessionState(timeLimit)

    var score: Int = score
        set(value) {
            sessionState.validateInProgress()
            if (value <= field) {
                throw ScoreCanOnlyBeIncreasedException()
            }
            field = value
        }

    val currentState: SessionStateType
        get() = sessionState.current

    fun pause() = sessionState.pause()
    fun resume() = sessionState.resume()
    fun end() = sessionState.end()
}

