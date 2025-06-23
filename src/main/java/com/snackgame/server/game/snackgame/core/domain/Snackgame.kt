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
    board: Board = Board(DEFAULT_HEIGHT, DEFAULT_WIDTH),
    timeLimit: Duration = SESSION_TIME + SPARE_TIME,
    score: Int = 0
) : Session(ownerId, timeLimit, score) {

    @Lob
    @Convert(converter = BoardConverter::class)
    var board = board
        private set

    @Deprecated("스트릭 구현 완료 시 제거")
    fun setScoreUnsafely(score: Int) {
        this.score = score
    }

    fun remove(streak: Streak) {
        val removedSnacks = board.removeSnacksIn(streak)
        this.score += streak.length
        if (removedSnacks.any(Snack::isGolden)) {
            this.board = board.reset()
        }
    }

    fun bomb(streak: Streak) {
        val removedSnacks = board.bombSnacksIn(streak)
        this.score += streak.length
        if (removedSnacks.any(Snack::isGolden)) {
            this.board = board.reset()
        }
    }

    override val metadata = SNACK_GAME

    companion object {
        const val DEFAULT_HEIGHT = 8
        const val DEFAULT_WIDTH = 6
        val SESSION_TIME: Duration = Duration.ofMinutes(2)
        val SPARE_TIME: Duration = Duration.ofSeconds(2)
    }
}
