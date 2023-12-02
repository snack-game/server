package com.snackgame.server.applegame.business.domain.game;

public interface BestScoreTransfer {

    void transfer(Long victimMemberId, Long currentMemberId);
}
