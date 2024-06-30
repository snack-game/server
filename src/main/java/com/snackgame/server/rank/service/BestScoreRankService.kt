package com.snackgame.server.rank.service

import com.snackgame.server.rank.controller.dto.RankResponseV2
import com.snackgame.server.rank.domain.BestScores
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BestScoreRankService(
    private val bestScores: BestScores
) {

    @Transactional(readOnly = true)
    fun rankLeadersBy(gameId: Long, seasonId: Long? = null): List<RankResponseV2> {
        return bestScores.rankLeadersBy(RANK_PAGE_SIZE, gameId, seasonId)
            .map { RankResponseV2.of(it) }
    }

    @Transactional(readOnly = true)
    fun rank(memberId: Long, gameId: Long, seasonId: Long? = null): RankResponseV2 {
        return RankResponseV2.of(bestScores.findRankOf(memberId, gameId, seasonId))
    }

    companion object {
        private const val RANK_PAGE_SIZE = 50
    }
}
