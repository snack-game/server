package com.snackgame.server.game.snackgame.domain

import com.snackgame.server.game.metadata.Metadata.SNACK_GAME_INFINITE
import com.snackgame.server.game.session.domain.Session
import javax.persistence.Entity

@Entity
class SnackgameInfinite(ownerId: Long) : Session(ownerId) {

    @Deprecated("스트릭 구현 시 제거 예정")
    fun setScoreUnsafely(score: Int) {
        this.score = score
    }

    override val metadata = SNACK_GAME_INFINITE
}
