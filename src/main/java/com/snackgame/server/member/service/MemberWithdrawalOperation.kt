package com.snackgame.server.member.service

interface MemberWithdrawalOperation {

    fun executeOn(memberId: Long)
}
