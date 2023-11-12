package com.snackgame.server.applegame.business.rank;

import org.springframework.stereotype.Component;

import com.snackgame.server.applegame.business.domain.BestScoreTransfer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SnackgameBestScoreTransfer implements BestScoreTransfer {

    private final BestScores bestScores;

    @Override
    public void transfer(Long victimMemberId, Long newMemberId) {
        bestScores.transfer(victimMemberId, newMemberId);
    }
}
