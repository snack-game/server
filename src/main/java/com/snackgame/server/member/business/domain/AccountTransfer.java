package com.snackgame.server.member.business.domain;

public interface AccountTransfer {

    void transferAll(Long victimMemberId, Long currentMemberId);
}
