package com.snackgame.server.rank.service

import com.snackgame.server.game.session.event.SessionEndEvent
import com.snackgame.server.member.domain.AccountType
import com.snackgame.server.member.domain.Member
import com.snackgame.server.member.domain.MemberRepository
import com.snackgame.server.rank.domain.BestScore
import com.snackgame.server.rank.domain.BestScores
import com.snackgame.server.rank.domain.Season
import com.snackgame.server.rank.domain.SeasonRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Service
class BestScoreRenewal(
    private val memberRepository: MemberRepository,
    private val seasonRepository: SeasonRepository,
    private val bestScores: BestScores
) {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun renewBestScoreWith(session: SessionEndEvent) {
        val owner: Member = memberRepository.getById(session.ownerId)
        if (owner.accountType == AccountType.GUEST) {
            return
        }

        val season: Season = seasonRepository.latest
        val bestScore = bestScores.getBy(season.id, session)
        bestScores.save(bestScore.renewWith(session.sessionId, session.score))
    }

    private fun BestScores.getBy(
        seasonId: Long,
        session: SessionEndEvent,
    ): BestScore =
        bestScores.findByOwnerIdAndGameIdAndSeasonId(session.ownerId, session.metadata.gameId, seasonId)
            ?: bestScores.save(
                BestScore(session.ownerId, session.metadata.gameId, seasonId, session.sessionId, session.score)
            )
}
