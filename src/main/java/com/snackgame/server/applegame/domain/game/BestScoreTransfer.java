package com.snackgame.server.applegame.domain.game;

public interface BestScoreTransfer {

    void transfer(Long victimMemberId, Long currentMemberId);
}
