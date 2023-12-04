package com.snackgame.server.member.domain;

public interface AccountTransfer {

    void transferAll(Long victimMemberId, Long currentMemberId);
}
