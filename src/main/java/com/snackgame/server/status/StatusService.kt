package com.snackgame.server.status

import com.snackgame.server.game.metadata.Metadata
import com.snackgame.server.game.session.event.SessionEndEvent
import com.snackgame.server.member.domain.MemberRepository
import com.snackgame.server.member.domain.getBy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Service
class StatusService(private val memberRepository: MemberRepository) {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Transactional
    fun onSessionEnd(session: SessionEndEvent) {
        when (session.metadata) {
            Metadata.SNACK_GAME,
            Metadata.APPLE_GAME,
            Metadata.SNACK_GAME_INFINITE,
            Metadata.SNACK_GAME_BIZ -> addExpWithScore(session)
        }
    }

    private fun addExpWithScore(session: SessionEndEvent) {
        val member = memberRepository.getBy(session.ownerId)
        member.status.addExp(session.score.toDouble())
    }
}
