package com.snackgame.server.rank;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.applegame.domain.game.AppleGame;
import com.snackgame.server.applegame.event.GameEndEvent;
import com.snackgame.server.member.domain.AccountType;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.member.domain.MemberRepository;
import com.snackgame.server.rank.controller.dto.RankResponseV2;
import com.snackgame.server.rank.domain.BestScore;
import com.snackgame.server.rank.domain.BestScores;
import com.snackgame.server.rank.domain.Season;
import com.snackgame.server.rank.domain.SeasonRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BestScoreRankingService {

    private static final int RANK_PAGE_SIZE = 50;

    private final BestScores bestScores;
    private final MemberRepository memberRepository;
    private final SeasonRepository seasonRepository;

    @Transactional(readOnly = true)
    public List<RankResponseV2> rankLeaders() {
        return bestScores.rankLeaders(RANK_PAGE_SIZE)
                .stream()
                .map(RankResponseV2::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RankResponseV2> rankLeadersBy(Long seasonId) {
        return bestScores.rankLeadersBy(seasonId, RANK_PAGE_SIZE)
                .stream()
                .map(RankResponseV2::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RankResponseV2 rank(Long memberId) {
        return RankResponseV2.of(bestScores.rank(memberId));
    }

    @Transactional(readOnly = true)
    public RankResponseV2 rank(Long memberId, Long seasonId) {
        return RankResponseV2.of(bestScores.rank(memberId, seasonId));
    }

    @EventListener
    @Transactional
    public void renewBestScoreWith(GameEndEvent event) {
        AppleGame appleGame = event.getAppleGame();
        Member owner = memberRepository.getById(appleGame.getOwnerId());
        if (owner.getAccountType() == AccountType.GUEST) {
            return;
        }

        Season season = seasonRepository.getLatest();
        bestScores.findByOwnerIdAndSeasonId(appleGame.getOwnerId(), season.getId())
                .orElseGet(() -> bestScores.save(
                        new BestScore(owner.getId(), season.getId())
                ))
                .renewWith(appleGame);
    }
}
