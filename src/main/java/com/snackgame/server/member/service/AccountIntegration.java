package com.snackgame.server.member.service;

public interface AccountIntegration {

    void execute(Long victimMemberId, Long currentMemberId);
}
