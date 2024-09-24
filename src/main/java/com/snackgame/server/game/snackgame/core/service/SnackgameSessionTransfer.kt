package com.snackgame.server.game.snackgame.core.service

import com.snackgame.server.game.snackgame.core.domain.SnackgameRepository
import com.snackgame.server.member.service.AccountIntegration
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class SnackgameSessionTransfer(
    private val snackgameRepository: SnackgameRepository
) : AccountIntegration {

    @Transactional(propagation = Propagation.MANDATORY)
    override fun execute(victimMemberId: Long, currentMemberId: Long) {
        val transferredCount = snackgameRepository.transferSessions(victimMemberId, currentMemberId)

        log.debug("memberId={}의 스낵게임 세션 {}개를 memberId={}에게 이전했습니다", victimMemberId, transferredCount, currentMemberId)
    }

    companion object {
        private val log = LoggerFactory.getLogger(SnackgameSessionTransfer::class.java)
    }
}
