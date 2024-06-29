package com.snackgame.server.rank.domain;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.applegame.domain.game.BestScoreTransfer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AppleGameBestScoreTransfer implements BestScoreTransfer {

    private final BestScores bestScores;
    private final SeasonRepository seasonRepository;

    @Override
    @Transactional
    public void transfer(Long victimMemberId, Long currentMemberId) {
        List<Season> seasons = seasonRepository.findAll();
        seasons.forEach(season -> transferIn(season, victimMemberId, currentMemberId));
    }

    private void transferIn(Season season, Long victimMemberId, Long currentMemberId) {
        BestScore victimBestScore = bestScores.getByOwnerIdAndSeasonId(victimMemberId, season.getId());
        BestScore currentBestScore = bestScores.getByOwnerIdAndSeasonId(currentMemberId, season.getId());

        if (victimBestScore != BestScore.EMPTY && victimBestScore.beats(currentBestScore)) {
            victimBestScore.transferTo(currentBestScore.getOwnerId());
            bestScores.delete(currentBestScore);
        }
    }
}
