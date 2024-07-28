package com.snackgame.server.member.service

import com.snackgame.server.member.domain.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class StatusTransfer(
    private val memberRepository: MemberRepository
) : AccountIntegration {

    @Transactional(propagation = Propagation.MANDATORY)
    override fun execute(victimMemberId: Long, currentMemberId: Long) {
        val victim = memberRepository.findByIdOrNull(victimMemberId)
        val currentMember = memberRepository.findByIdOrNull(currentMemberId)
        if (victim != null && currentMember != null) {
            currentMember.status.addExp(victim.status.totalExp)

            log.debug(
                "memberId={}의 총 경험치 {}를 memberId={}에게 이전했습니다",
                victim.id,
                victim.status.totalExp,
                currentMember.id
            )
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(StatusTransfer::class.java)
    }
}
