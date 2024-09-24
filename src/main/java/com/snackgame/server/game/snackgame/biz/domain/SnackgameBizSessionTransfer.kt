package com.snackgame.server.game.snackgame.biz.domain

import com.snackgame.server.member.service.AccountIntegration
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class SnackgameBizSessionTransfer(
    private val snackgameBizRepository: SnackgameBizRepository
) : AccountIntegration {

    @Transactional(propagation = Propagation.MANDATORY)
    override fun execute(victimMemberId: Long, currentMemberId: Long) {
        val transferredCount = snackgameBizRepository.transferSessions(victimMemberId, currentMemberId)

        log.debug(
            "memberId={}의 스낵게임 Biz 세션 {}개를 memberId={}에게 이전했습니다",
            victimMemberId,
            transferredCount,
            currentMemberId
        )
    }

    companion object {
        private val log = LoggerFactory.getLogger(SnackgameBizSessionTransfer::class.java)
    }
}
