package com.snackgame.server.game.snackgame.infinite.service

import com.snackgame.server.game.snackgame.infinite.domain.SnackgameInifiniteRepository
import com.snackgame.server.member.service.MemberWithdrawalOperation
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class SnackgameInfiniteSessionWithdrawal(
    private val snackgameInfiniteRepository: SnackgameInifiniteRepository
) : MemberWithdrawalOperation {

    @Transactional(propagation = Propagation.MANDATORY)
    override fun executeOn(memberId: Long) {
        snackgameInfiniteRepository.deleteAllByOwnerId(memberId)
    }
}
