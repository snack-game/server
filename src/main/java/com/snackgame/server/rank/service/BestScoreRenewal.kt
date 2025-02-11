package com.snackgame.server.rank.service

import com.snackgame.server.game.session.event.SessionEndEvent
import com.snackgame.server.member.domain.AccountType
import com.snackgame.server.member.service.MemberAccountService
import com.snackgame.server.rank.domain.BestScore
import com.snackgame.server.rank.domain.BestScores
import com.snackgame.server.rank.domain.SeasonRepository
import com.snackgame.server.rank.event.BestScoreRenewalEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Service
class BestScoreRenewal(
    private val seasonRepository: SeasonRepository,
    private val bestScores: BestScores,
    private val memberAccountService: MemberAccountService,
    private val eventPublisher: ApplicationEventPublisher
) {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Transactional(propagation = Propagation.MANDATORY)
    fun renewBestScoreWith(session: SessionEndEvent) {
        val bestScore = getOrCreateBestScoreBy(seasonRepository.latest.id, session)
        bestScore.renewWith(session.sessionId, session.score)
            .also { bestScores.save(it) }

        val rankAndOwner = bestScores.findRankOf(session.ownerId, session.metadata.gameId, seasonRepository.latest.id)

        eventPublisher.publishEvent(BestScoreRenewalEvent.of(session, rankAndOwner?.rank))
    }

    private fun getOrCreateBestScoreBy(seasonId: Long, session: SessionEndEvent): BestScore {
        val bestScore = bestScores.findByOwnerIdAndGameIdAndSeasonId(session.ownerId, session.metadata.gameId, seasonId)
        if (bestScore != null) {
            return bestScore
        }
        val newBestScore = BestScore(
            session.ownerId,
            session.metadata.gameId,
            seasonId,
            session.sessionId,
            session.score,
            memberAccountService.getBy(session.ownerId).type != AccountType.GUEST
        )
        return bestScores.save(newBestScore)
    }
}
