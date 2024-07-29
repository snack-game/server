package com.snackgame.server.rank.service

import com.snackgame.server.game.metadata.Metadata
import com.snackgame.server.member.service.AccountIntegration
import com.snackgame.server.rank.domain.BestScores
import com.snackgame.server.rank.domain.SeasonRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class BestScoreTransfer(
    private val bestScores: BestScores,
    private val seasonRepository: SeasonRepository
) : AccountIntegration {

    @Transactional(propagation = Propagation.MANDATORY)
    override fun execute(victimMemberId: Long, currentMemberId: Long) {
        val gameIds = Metadata.entries.map { it.gameId }
        val seasons = seasonRepository.findAll()
        gameIds.forEach { gameId ->
            seasons.forEach { season -> transfer(gameId, season.id, victimMemberId, currentMemberId) }
        }

        log.debug("memberId={}의 최고점수들을 memberId={}에게 이전했습니다", victimMemberId, currentMemberId)
    }

    private fun transfer(gameId: Long, seasonId: Long, victimMemberId: Long, currentMemberId: Long) {
        val victimBestScore = bestScores.findByOwnerIdAndGameIdAndSeasonId(victimMemberId, gameId, seasonId)
        val currentBestScore = bestScores.findByOwnerIdAndGameIdAndSeasonId(currentMemberId, gameId, seasonId)

        if (victimBestScore == null) {
            return
        }
        if (currentBestScore == null) {
            bestScores.save(victimBestScore.transferTo(currentMemberId))
            return
        }
        if (victimBestScore.beats(currentBestScore)) {
            bestScores.save(currentBestScore.renewWith(victimBestScore.sessionId, victimBestScore.score))
            bestScores.delete(victimBestScore)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(BestScoreTransfer::class.java)
    }
}
