package com.snackgame.server.applegame.business;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.applegame.business.domain.Ranking;
import com.snackgame.server.applegame.business.exception.NoRankingYetException;
import com.snackgame.server.applegame.controller.dto.RankingResponse;
import com.snackgame.server.applegame.dao.SessionRankingDao;
import com.snackgame.server.applegame.dao.SessionRankingDao2;
import com.snackgame.server.applegame.dao.dto.RankingDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AppleGameRankingService {

    private static final int RANKING_PAGE_SIZE = 50;

    private final SessionRankingDao sessionRankingDao;
    private final AppleGameService appleGameService;
    private final SessionRankingDao2 sessionRankingDao2;

    @Transactional(readOnly = true)
    public List<RankingResponse> getEntireRankings() {
        List<Ranking> top50Rankings = sessionRankingDao2.selectTopsByScoreIn(RANKING_PAGE_SIZE);

        return top50Rankings.stream()
                .map(it -> RankingResponse.of(it.getRanking(), it.getSession()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RankingResponse getBestRankingOf(Long memberId) {
        return sessionRankingDao2.selectBestByScoreOf(memberId)
                .map(it -> RankingResponse.of(it.getRanking(), it.getSession()))
                .orElseThrow(NoRankingYetException::new);
        // return sessionRankingDao.selectBestByScoreOf(memberId)
        //         .map(this::getResponseOf)
        //         .orElseThrow(NoRankingYetException::new);
    }

    private RankingResponse getResponseOf(RankingDto rankingDto) {
        return RankingResponse.of(
                rankingDto.getRanking(),
                appleGameService.findBy(rankingDto.getSessionId())
        );
    }
}
