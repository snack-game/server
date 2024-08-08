package com.snackgame.server.applegame.service

import com.snackgame.server.applegame.domain.game.AppleGames
import com.snackgame.server.member.service.MemberWithdrawalOperation
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class AppleGameSessionWithdrawal(
    private val appleGames: AppleGames
) : MemberWithdrawalOperation {

    @Transactional(propagation = Propagation.MANDATORY)
    override fun executeOn(memberId: Long) {
        appleGames.deleteAllByOwnerId(memberId)
    }
}
