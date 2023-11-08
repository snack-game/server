package com.snackgame.server.applegame.business;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.snackgame.server.applegame.business.domain.AppleGame;
import com.snackgame.server.applegame.business.domain.AppleGameSessionRepository;
import com.snackgame.server.applegame.business.event.GameEndEvent;
import com.snackgame.server.applegame.business.exception.NoRankingYetException;
import com.snackgame.server.applegame.business.rank.BestScore;
import com.snackgame.server.applegame.business.rank.BestScores;
import com.snackgame.server.applegame.controller.dto.RankResponseV2;
import com.snackgame.server.applegame.controller.dto.RankingResponse;
import com.snackgame.server.applegame.dao.SessionRankingDao;
import com.snackgame.server.applegame.dao.dto.RankingDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AppleGameRankingService {

    private static final int RANKING_PAGE_SIZE = 50;

    private final SessionRankingDao sessionRankingDao;
    private final AppleGameSessionRepository sessions;
    private final BestScores bestScores;

    @Transactional(readOnly = true)
    public List<RankResponseV2> rank50ByBestScore() {
        return bestScores.rank(RANKING_PAGE_SIZE)
                .stream()
                .map(RankResponseV2::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RankResponseV2 rankByBestScoreOf(Long memberId) {
        return RankResponseV2.of(bestScores.rank(memberId));
    }

    @TransactionalEventListener
    @Transactional
    public void registerBestScoreWith(GameEndEvent event) {
        AppleGame appleGame = event.getAppleGame();
        BestScore bestScore = bestScores.getByOwnerId(appleGame.getOwner().getId());
        bestScore.renewWith(appleGame);
    }

    @Deprecated
    @Transactional(readOnly = true)
    public List<RankingResponse> getEntireRankings() {
        List<RankingDto> top50Rankings = sessionRankingDao.selectTopsByScoreIn(RANKING_PAGE_SIZE);

        return top50Rankings.stream()
                .map(this::getResponseOf)
                .collect(Collectors.toList());
    }

    @Deprecated
    @Transactional(readOnly = true)
    public RankingResponse getBestRankingOf(Long memberId) {
        return sessionRankingDao.selectBestByScoreOf(memberId)
                .map(this::getResponseOf)
                .orElseThrow(NoRankingYetException::new);
    }

    @Deprecated
    private RankingResponse getResponseOf(RankingDto rankingDto) {
        return RankingResponse.of(
                rankingDto.getRanking(),
                sessions.getBy(rankingDto.getSessionId())
        );
    }
}
