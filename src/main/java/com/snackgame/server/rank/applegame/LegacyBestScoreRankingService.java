package com.snackgame.server.rank.applegame;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.applegame.domain.game.AppleGames;
import com.snackgame.server.applegame.exception.NoRankingYetException;
import com.snackgame.server.rank.applegame.controller.dto.RankingResponse;
import com.snackgame.server.rank.applegame.dao.SessionRankingDao;
import com.snackgame.server.rank.applegame.dao.dto.RankingDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Deprecated(forRemoval = true)
@Service
public class LegacyBestScoreRankingService {

    private static final int RANK_PAGE_SIZE = 50;

    private final SessionRankingDao sessionRankingDao;
    private final AppleGames appleGames;

    // TODO: Deprecated 된 기존 방식을 개선 정도(MTT) 테스트 후 제거
    @Deprecated(forRemoval = true)
    @Transactional(readOnly = true)
    public List<RankingResponse> getEntireRankings() {
        List<RankingDto> top50Rankings = sessionRankingDao.selectTopsByScoreIn(RANK_PAGE_SIZE);

        return top50Rankings.stream()
                .map(this::getResponseOf)
                .collect(Collectors.toList());
    }

    @Deprecated(forRemoval = true)
    @Transactional(readOnly = true)
    public RankingResponse getBestRankingOf(Long memberId) {
        return sessionRankingDao.selectBestByScoreOf(memberId)
                .map(this::getResponseOf)
                .orElseThrow(NoRankingYetException::new);
    }

    @Deprecated(forRemoval = true)
    private RankingResponse getResponseOf(RankingDto rankingDto) {
        return RankingResponse.of(
                rankingDto.getRanking(),
                appleGames.getBy(rankingDto.getSessionId())
        );
    }
}
