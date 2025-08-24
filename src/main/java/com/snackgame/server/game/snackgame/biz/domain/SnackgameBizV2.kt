package com.snackgame.server.game.snackgame.biz.domain

import com.snackgame.server.game.metadata.Metadata.SNACK_GAME_BIZ_V2
import com.snackgame.server.game.session.domain.Session
import com.snackgame.server.game.snackgame.core.domain.Board
import com.snackgame.server.game.snackgame.core.domain.BoardConverter
import com.snackgame.server.game.snackgame.core.domain.Snackgame.Companion.DEFAULT_HEIGHT
import com.snackgame.server.game.snackgame.core.domain.Snackgame.Companion.DEFAULT_WIDTH
import com.snackgame.server.game.snackgame.core.domain.Snackgame.Companion.FEVER_MULTIPLIER
import com.snackgame.server.game.snackgame.core.domain.Snackgame.Companion.NORMAL_MULTIPLIER
import com.snackgame.server.game.snackgame.core.domain.Snackgame.Companion.SESSION_TIME
import com.snackgame.server.game.snackgame.core.domain.Snackgame.Companion.SPARE_TIME
import com.snackgame.server.game.snackgame.core.domain.Streak
import com.snackgame.server.game.snackgame.core.domain.item.FeverTime
import com.snackgame.server.game.snackgame.core.domain.snack.Snack
import com.snackgame.server.game.snackgame.core.service.dto.StreakWithFever
import java.time.Duration
import javax.persistence.Convert
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Lob

@Entity
open class SnackgameBizV2(
    ownerId: Long,
    board: Board = Board(DEFAULT_HEIGHT, DEFAULT_WIDTH),
    timeLimit: Duration = SESSION_TIME + SPARE_TIME,
    score: Int = 0
) : Session(ownerId, timeLimit, score) {

    @Lob
    @Convert(converter = BoardConverter::class)
    var board = board
        private set

    @Embedded
    var feverTime: FeverTime? = null
        private set

    fun remove(streak: Streak) {
        val removedSnacks = board.removeSnacksIn(streak)
        this.score += removedSnacks.size
        if (removedSnacks.any(Snack::isGolden)) {
            this.board = board.reset()
        }
    }

    fun remove(streakWithFever: StreakWithFever) {
        val streak = streakWithFever.streak
        val removedSnacks = board.removeSnacksIn(streak)

        val serverIsFever = feverTime?.isActive(streakWithFever.occurredAt) == true
        val isValid = streakWithFever.clientIsFever && serverIsFever

        val multiplier = if (isValid) FEVER_MULTIPLIER else NORMAL_MULTIPLIER
        increaseScore(streak.length * multiplier)

        if (removedSnacks.any(Snack::isGolden)) {
            this.board = board.reset()
        }
    }

    private fun increaseScore(earn: Int) {
        val multiplier = if (feverTime?.isActive() == true) FEVER_MULTIPLIER else NORMAL_MULTIPLIER
        this.score += earn * multiplier
    }

    override val metadata = SNACK_GAME_BIZ_V2
}
