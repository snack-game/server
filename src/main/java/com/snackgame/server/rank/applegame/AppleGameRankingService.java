package com.snackgame.server.rank.applegame;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.applegame.domain.game.AppleGame;
import com.snackgame.server.applegame.domain.game.AppleGames;
import com.snackgame.server.applegame.event.GameEndEvent;
import com.snackgame.server.applegame.exception.NoRankingYetException;
import com.snackgame.server.member.domain.AccountType;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.member.domain.MemberRepository;
import com.snackgame.server.rank.applegame.controller.dto.RankResponseV2;
import com.snackgame.server.rank.applegame.controller.dto.RankingResponse;
import com.snackgame.server.rank.applegame.dao.SessionRankingDao;
import com.snackgame.server.rank.applegame.dao.dto.RankingDto;
import com.snackgame.server.rank.applegame.domain.BestScore;
import com.snackgame.server.rank.applegame.domain.BestScores;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AppleGameRankingService {

    private static final int RANKING_PAGE_SIZE = 50;

    private final SessionRankingDao sessionRankingDao;
    private final AppleGames appleGames;
    private final BestScores bestScores;

    private final MemberRepository memberRepository;

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

    @EventListener
    @Transactional
    public void renewBestScoreWith(GameEndEvent event) {
        AppleGame appleGame = event.getAppleGame();
        Member owner = memberRepository.getById(appleGame.getOwnerId());
        if (owner.getAccountType() != AccountType.GUEST) {
            BestScore bestScore = bestScores.getByOwnerId(appleGame.getOwnerId());
            bestScore.renewWith(appleGame);
        }
    }

    // TODO: Deprecated 된 기존 방식을 개선 정도(MTT) 테스트 후 제거
    @Deprecated(forRemoval = true)
    @Transactional(readOnly = true)
    public List<RankingResponse> getEntireRankings() {
        List<RankingDto> top50Rankings = sessionRankingDao.selectTopsByScoreIn(RANKING_PAGE_SIZE);

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
