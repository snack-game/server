package com.snackgame.server.game.session.domain

import com.snackgame.server.common.domain.BaseEntity
import com.snackgame.server.game.metadata.Metadata
import com.snackgame.server.game.session.exception.ScoreCannotBeDecreased
import java.time.Duration
import java.time.LocalDateTime
import javax.persistence.Embedded
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class Session(
    open val ownerId: Long,
    timeLimit: Duration = SessionState.TTL,
    score: Int = 0,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val sessionId: Long = 0
) : BaseEntity() {
    @Embedded
    private val sessionState = SessionState(timeLimit)

    open var score: Int = score
        protected set(value) {
            sessionState.validateInProgress()
            if (value < field) {
                throw ScoreCannotBeDecreased()
            }
            field = value
        }

    val currentState: SessionStateType
        get() = sessionState.current

    abstract val metadata: Metadata

    fun pause(): LocalDateTime = sessionState.pause()
    fun resume(): LocalDateTime = sessionState.resume()
    fun end() = sessionState.end()
}

