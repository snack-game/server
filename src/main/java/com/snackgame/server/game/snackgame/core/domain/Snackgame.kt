package com.snackgame.server.game.snackgame.core.domain

import com.snackgame.server.game.metadata.Metadata.SNACK_GAME
import com.snackgame.server.game.session.domain.Session
import com.snackgame.server.game.snackgame.core.domain.item.FeverTime
import com.snackgame.server.game.snackgame.core.domain.snack.Snack
import com.snackgame.server.game.snackgame.core.service.dto.StreakWithFever
import java.time.Duration
import javax.persistence.Convert
import javax.persistence.Embedded
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
    open var board = board
        protected set

    @Embedded
    open var feverTime: FeverTime? = null
        protected set

    @Deprecated("스트릭 구현 완료 시 제거")
    fun setScoreUnsafely(score: Int) {
        this.score = score
    }

    //todo : 제거 예정
    fun remove(streak: Streak) {
        val removedSnacks = board.removeSnacksIn(streak)
        increaseScore(streak.length)

        if (removedSnacks.any(Snack::isGolden)) {
            this.board = board.reset()
        }
    }

    fun remove(streakWithFever: StreakWithFever) {
        val streak = streakWithFever.streak
        val removedSnacks = board.removeSnacksIn(streak)

        val multiplier = calculateMultiplier(streakWithFever)
        increaseScore(streak.length * multiplier)

        if (removedSnacks.any(Snack::isGolden)) {
            this.board = board.reset()
        }
    }

    fun removeBomb(streak: Streak) {
        val removedSnacks = board.bombSnacksIn(streak)
        increaseScore(removedSnacks.size)

        if (removedSnacks.any(Snack::isGolden)) {
            this.board = board.reset()
        }
    }

    private fun calculateMultiplier(streakWithFever: StreakWithFever): Int {
        val serverFever = feverTime ?: return NORMAL_MULTIPLIER

        if (streakWithFever.clientIsFever &&
            serverFever.isFeverTime(streakWithFever.occurredAt) &&
            serverFever.canApplyFeverMultiplier()
        ) {
            serverFever.incrementFeverStreak()
            return FEVER_MULTIPLIER
        }
        return NORMAL_MULTIPLIER
    }


    private fun increaseScore(earn: Int) {
        this.score += earn
    }

    fun startFeverTime() {
        this.feverTime = FeverTime.start()
    }

    override val metadata = SNACK_GAME

    companion object {
        const val DEFAULT_HEIGHT = 8
        const val DEFAULT_WIDTH = 6
        const val FEVER_MULTIPLIER = 2
        const val NORMAL_MULTIPLIER = 1
        val SESSION_TIME: Duration = Duration.ofMinutes(2)
        val SPARE_TIME: Duration = Duration.ofSeconds(2)
    }
}
