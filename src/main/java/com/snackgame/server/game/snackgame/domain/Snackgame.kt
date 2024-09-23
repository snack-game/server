package com.snackgame.server.game.snackgame.domain

import com.snackgame.server.game.metadata.Metadata
import com.snackgame.server.game.metadata.Metadata.SNACK_GAME
import com.snackgame.server.game.session.domain.Session
import com.snackgame.server.game.snackgame.snack.Snack
import java.time.Duration
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.Lob

@Entity
class Snackgame(
    ownerId: Long,
    @Lob
    @Convert(converter = BoardConverter::class)
    val board: Board,
    timeLimit: Duration = SESSION_TIME + SPARE_TIME,
    score: Int = 0
) : Session(ownerId, timeLimit, score) {


    @Deprecated("스트릭 구현 시 제거 예정")
    fun setScoreUnsafely(score: Int) {
        this.score = score
    }


    fun removeSnacks(streak: Streak) {
        streak.validateStreak()
        val removedSnacks = board.removeSnacksIn(streak)

        if (removedSnacks.stream().anyMatch(Snack::isGolden)) {
            board.reset()
        }
        increaseScore(removedSnacks.size)
    }

    private fun increaseScore(size: Int) {
        this.score += size
    }

    fun getSnacks(): List<List<Snack>> {
        return board.getSnacks()
    }

    override val metadata = SNACK_GAME

    companion object {
        private const val DEFAULT_HEIGHT = 8
        private const val DEFAULT_WIDTH = 6
        private val SESSION_TIME = Duration.ofMinutes(2)
        private val SPARE_TIME = Duration.ofSeconds(2)

        fun ofRandomized(ownerId: Long): Snackgame {
            return Snackgame(ownerId, Board(DEFAULT_HEIGHT, DEFAULT_WIDTH))
        }
    }
}
