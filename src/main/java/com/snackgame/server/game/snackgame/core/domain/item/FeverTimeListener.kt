package com.snackgame.server.game.snackgame.core.domain.item

import com.snackgame.server.game.session.event.SessionPauseEvent
import com.snackgame.server.game.session.event.SessionResumeEvent
import com.snackgame.server.game.snackgame.core.domain.SnackgameRepository
import com.snackgame.server.game.snackgame.core.domain.getBy
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class FeverTimeListener(
    private val snackgameRepository: SnackgameRepository
) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun onSessionPaused(event: SessionPauseEvent) {
        val game = snackgameRepository.getBy(event.ownerId, event.sessionId)
        game.feverTime?.pause()
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun onSessionResumed(event: SessionResumeEvent) {
        val game = snackgameRepository.getBy(event.ownerId, event.sessionId)
        game.feverTime?.resume()
    }
}