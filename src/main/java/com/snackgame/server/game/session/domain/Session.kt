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
    timeLimit: Duration = SessionStatus.TTL,
    score: Int = 0,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val sessionId: Long = 0
) : BaseEntity() {
    @Embedded
    private val sessionStatus = SessionStatus(timeLimit)

    var score: Int = score
        set(value) {
            sessionStatus.validateInProgress()
            if (value <= field) {
                throw ScoreCanOnlyBeIncreasedException()
            }
            field = value
        }

    val currentStatus: SessionStatusType
        get() = sessionStatus.current

    fun pause() = sessionStatus.pause()
    fun resume() = sessionStatus.resume()
    fun end() = sessionStatus.end()
}

