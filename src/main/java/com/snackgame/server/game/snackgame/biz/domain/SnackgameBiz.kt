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
    @Lob
    @Convert(converter = BoardConverter::class)
    val board: Board = Board(DEFAULT_HEIGHT, DEFAULT_WIDTH),
    timeLimit: Duration = SESSION_TIME + SPARE_TIME,
    score: Int = 0
) : Session(ownerId, timeLimit, score) {

    @Deprecated("스트릭 구현 시 제거 예정")
    fun setScoreUnsafely(score: Int) {
        this.score = score
    }

    fun remove(streak: Streak) {
        val removedSnacks = board.removeSnacksIn(streak)
        if (removedSnacks.any(Snack::isGolden)) {
            board.reset()
        }
        this.score += removedSnacks.size
    }

    override val metadata = SNACK_GAME_BIZ
}
