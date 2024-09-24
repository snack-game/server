package com.snackgame.server.game.snackgame.core.service

import com.snackgame.server.game.snackgame.core.domain.SnackgameRepository
import com.snackgame.server.member.service.MemberWithdrawalOperation
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class SnackgameSessionWithdrawal(
    private val snackgameRepository: SnackgameRepository
) : MemberWithdrawalOperation {

    @Transactional(propagation = Propagation.MANDATORY)
    override fun executeOn(memberId: Long) {
        snackgameRepository.deleteAllByOwnerId(memberId)
    }
}
