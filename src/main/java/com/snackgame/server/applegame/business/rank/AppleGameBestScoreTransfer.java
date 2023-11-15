package com.snackgame.server.applegame.business.rank;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.applegame.business.domain.BestScoreTransfer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppleGameBestScoreTransfer implements BestScoreTransfer {

    private final BestScores bestScores;

    @Override
    @Transactional
    public void transfer(Long victimMemberId, Long currentMemberId) {
        BestScore victimBestScore = bestScores.getByOwnerId(victimMemberId);
        BestScore currentBestScore = bestScores.getByOwnerId(currentMemberId);

        if (victimBestScore.beats(currentBestScore)) {
            currentBestScore.overwriteWith(victimBestScore);
        }
        bestScores.delete(victimBestScore);
    }
}
