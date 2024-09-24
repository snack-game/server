package com.snackgame.server.game.snackgame.core.domain

import com.snackgame.server.game.metadata.Metadata.SNACK_GAME
import com.snackgame.server.game.session.domain.Session
import com.snackgame.server.game.snackgame.core.domain.snack.Snack
import java.time.Duration
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.Lob

@Entity
open class Snackgame(
    ownerId: Long,
    @Lob
    @Convert(converter = BoardConverter::class)
    val board: Board,
    timeLimit: Duration = SESSION_TIME + SPARE_TIME,
    score: Int = 0
) : Session(ownerId, timeLimit, score) {

    @Deprecated("스트릭 구현 완료 시 제거")
    fun setScoreUnsafely(score: Int) {
        this.score = score
    }

    fun removeSnacks(streak: Streak) {
        val removedSnacks = board.removeSnacksIn(streak)
        if (removedSnacks.any(Snack::isGolden)) {
            board.reset()
        }
        this.score += streak.length
    }

    @Deprecated("대체", ReplaceWith("board.getSnacks()"))
    fun getSnacks(): List<List<Snack>> {
        return board.getSnacks()
    }

    override val metadata = SNACK_GAME

    companion object {
        const val DEFAULT_HEIGHT = 8
        const val DEFAULT_WIDTH = 6
        val SESSION_TIME = Duration.ofMinutes(2)
        val SPARE_TIME = Duration.ofSeconds(2)

        fun ofRandomized(ownerId: Long): Snackgame {
            return Snackgame(ownerId, Board(DEFAULT_HEIGHT, DEFAULT_WIDTH))
        }
    }
}
