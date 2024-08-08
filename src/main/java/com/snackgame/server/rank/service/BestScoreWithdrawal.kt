package com.snackgame.server.rank.service

import com.snackgame.server.member.service.MemberWithdrawalOperation
import com.snackgame.server.rank.domain.BestScores
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class BestScoreWithdrawal(
    private val bestScores: BestScores
) : MemberWithdrawalOperation {

    @Transactional(propagation = Propagation.MANDATORY)
    override fun executeOn(memberId: Long) {
        bestScores.deleteAllByOwnerId(memberId)
    }
}
