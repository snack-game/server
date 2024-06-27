package com.snackgame.server.member.service;

public interface AccountTransfer {

    void transferAll(Long victimMemberId, Long currentMemberId);
}
