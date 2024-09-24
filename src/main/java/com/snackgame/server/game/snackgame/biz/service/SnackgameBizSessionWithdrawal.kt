package com.snackgame.server.game.snackgame.biz.service

import com.snackgame.server.game.snackgame.biz.domain.SnackgameBizRepository
import com.snackgame.server.member.service.MemberWithdrawalOperation
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class SnackgameBizSessionWithdrawal(
    private val snackgameBizRepository: SnackgameBizRepository
) : MemberWithdrawalOperation {

    @Transactional(propagation = Propagation.MANDATORY)
    override fun executeOn(memberId: Long) {
        snackgameBizRepository.deleteAllByOwnerId(memberId)
    }
}
