package com.snackgame.server.game.snackgame.domain

import com.snackgame.server.game.metadata.Metadata
import com.snackgame.server.game.metadata.Metadata.SNACK_GAME
import com.snackgame.server.game.session.domain.Session
import java.time.Duration
import javax.persistence.Entity

@Entity
class Snackgame(ownerId: Long) : Session(ownerId, Duration.ofMinutes(2)) {

    @Deprecated("스트릭 구현 시 제거 예정")
    fun setScoreUnsafely(score: Int) {
        this.score = score
    }

    override fun getMetadata(): Metadata = SNACK_GAME
}
