package com.snackgame.server.rank.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Table(
    uniqueConstraints = [UniqueConstraint(
        name = "unique_game_best_score_in_a_season",
        columnNames = ["gameId", "seasonId", "ownerId"]
    )]
)
@Entity
class BestScore(
    val ownerId: Long,
    val gameId: Long,
    val seasonId: Long,
    val sessionId: Long,
    val score: Int = 0,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
) {

    fun beats(other: BestScore): Boolean {
        return this.score >= other.score
    }

    fun renewWith(sessionId: Long, score: Int): BestScore {
        if (score >= this.score) {
            return BestScore(ownerId, gameId, seasonId, sessionId, score, id)
        }
        return this
    }

    fun transferTo(ownerId: Long): BestScore {
        return BestScore(ownerId, gameId, seasonId, sessionId, score, id)
    }
}
