package com.snackgame.server.status

import com.snackgame.server.member.domain.MemberRepository
import com.snackgame.server.member.domain.getBy
import com.snackgame.server.member.service.MemberWithdrawalOperation
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class StatusWithdrawal(
    private val memberRepository: MemberRepository
) : MemberWithdrawalOperation {

    @Transactional(propagation = Propagation.MANDATORY)
    override fun executeOn(memberId: Long) {
        // TODO: 도메인 자체를 분리한 후, 해당 도메인 엔티티를 삭제하도록 개선한다 (BestScoreWithdrawal 참고)
        val member = memberRepository.getBy(memberId)

        member.status.reset()
    }
}
