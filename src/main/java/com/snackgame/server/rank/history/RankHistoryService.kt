package com.snackgame.server.rank.history

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RankHistoryService(
    private val rankHistories: RankHistories
) {

    @Transactional(readOnly = true)
    fun findMemberBelow(ownerId: Long): List<RankHistoryWithName> {
        val rankDifference = rankHistories.findRankDifference(ownerId) ?: 0
        val limitSize = minOf(MEMBER_SIZE, rankDifference)
        return rankHistories.findBelowWithName(ownerId, limitSize)
    }

    companion object {
        private const val MEMBER_SIZE = 5
    }
}
