package com.snackgame.server.rank.history

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RankHistoryService(
    private val rankHistories: RankHistories
) {

    @Transactional(readOnly = true)
    fun findMemberBelow(ownerId: Long): List<RankHistoryWithName> {
        return rankHistories.findBelowWithName(ownerId, MEMBER_SIZE)

    }

    companion object {
        private const val MEMBER_SIZE = 5
    }
}
