package com.snackgame.server.applegame.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.applegame.business.domain.AppleGame;
import com.snackgame.server.applegame.controller.dto.RankingResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AppleGameRankingService {

    private static final int RANKING_PAGE_SIZE = 50;
    private static final int FIRST_PAGE = 0;

    private final AppleGameService appleGameService;

    @Transactional(readOnly = true)
    public List<RankingResponse> getEntireRankings() {
        List<AppleGame> endedGames = appleGameService.getEndedGamesAt(FIRST_PAGE, RANKING_PAGE_SIZE);

        List<RankingResponse> rankingResponses = new ArrayList<>();
        for (int index = 0; index < endedGames.size(); index++) {
            rankingResponses.add(RankingResponse.of(
                    calculateTotalIndexWith(FIRST_PAGE, index),
                    endedGames.get(index)
            ));
        }
        return rankingResponses;
    }

    private int calculateTotalIndexWith(int page, int indexInPage) {
        return (1 + page) * (1 + indexInPage);
    }
}
