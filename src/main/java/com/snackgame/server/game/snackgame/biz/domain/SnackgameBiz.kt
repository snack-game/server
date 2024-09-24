package com.snackgame.server.game.snackgame.biz.domain

import com.snackgame.server.game.metadata.Metadata.SNACK_GAME_BIZ
import com.snackgame.server.game.session.domain.Session
import com.snackgame.server.game.snackgame.core.domain.Board
import com.snackgame.server.game.snackgame.core.domain.BoardConverter
import com.snackgame.server.game.snackgame.core.domain.Snackgame.Companion.DEFAULT_HEIGHT
import com.snackgame.server.game.snackgame.core.domain.Snackgame.Companion.DEFAULT_WIDTH
import com.snackgame.server.game.snackgame.core.domain.Snackgame.Companion.SESSION_TIME
import com.snackgame.server.game.snackgame.core.domain.Snackgame.Companion.SPARE_TIME
import com.snackgame.server.game.snackgame.core.domain.Streak
import com.snackgame.server.game.snackgame.core.domain.snack.Snack
import java.time.Duration
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.Lob

@Entity
open class SnackgameBiz(
    ownerId: Long,
    board: Board = Board(DEFAULT_HEIGHT, DEFAULT_WIDTH),
    timeLimit: Duration = SESSION_TIME + SPARE_TIME,
    score: Int = 0
) : Session(ownerId, timeLimit, score) {

    @Lob
    @Convert(converter = BoardConverter::class)
    var board = board
        private set

    @Deprecated("스트릭 구현 시 제거 예정")
    fun setScoreUnsafely(score: Int) {
        this.score = score
    }

    fun remove(streak: Streak) {
        val removedSnacks = board.removeSnacksIn(streak)
        this.score += removedSnacks.size
        if (removedSnacks.any(Snack::isGolden)) {
            this.board = board.reset()
        }
    }

    override val metadata = SNACK_GAME_BIZ
}
