package com.snackgame.server.rank.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.auth.token.support.Authenticated;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.rank.BestScoreRankingService;
import com.snackgame.server.rank.LegacyBestScoreRankingService;
import com.snackgame.server.rank.controller.dto.RankResponseV2;
import com.snackgame.server.rank.controller.dto.RankingResponse;
import com.snackgame.server.rank.domain.Season;
import com.snackgame.server.rank.domain.SeasonRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "⭐️ 랭킹")
@RestController
@RequiredArgsConstructor
public class RankingController {

    private final BestScoreRankingService bestScoreRankingService;
    private final LegacyBestScoreRankingService legacyBestScoreRankingService;
    private final SeasonRepository seasonRepository;

    @Operation(summary = "전체 시즌 - 선두 랭크 조회", description = "전체 시즌에서 랭킹을 선두 50등까지 조회한다")
    @GetMapping("/rankings/{gameId}")
    public List<RankResponseV2> showLeadingRanksBy(
            @PathVariable Long gameId,
            @RequestParam("by") RankingController.Criteria criteria
    ) {
        return bestScoreRankingService.rankLeaders();
    }

    @Operation(summary = "전체 시즌 - 자신의 랭크 조회", description = "전체 시즌에서 자신의 랭킹을 조회한다")
    @GetMapping("/rankings/{gameId}/me")
    public RankResponseV2 showRankOf(
            @Authenticated Member member,
            @PathVariable Long gameId,
            @RequestParam("by") RankingController.Criteria criteria) {
        return bestScoreRankingService.rank(member.getId());
    }

    @Operation(summary = "선두 랭크 조회", description = "특정 시즌에서 랭킹을 선두 50등까지 조회한다")
    @GetMapping("/rankings/{seasonId}/{gameId}")
    public List<RankResponseV2> showLeadingRanksBy(
            @PathVariable Long seasonId,
            @PathVariable Long gameId,
            @RequestParam("by") Criteria criteria
    ) {
        return bestScoreRankingService.rankLeadersBy(seasonId);
    }

    @Operation(summary = "자신의 랭크 조회", description = "특정 시즌에서 자신의 랭킹을 조회한다")
    @GetMapping("/rankings/{seasonId}/{gameId}/me")
    public RankResponseV2 showRankOf(
            @Authenticated Member member,
            @PathVariable Long seasonId,
            @PathVariable Long gameId,
            @RequestParam("by") Criteria criteria
    ) {
        return bestScoreRankingService.rank(member.getId(), seasonId);
    }

    @Operation(summary = "모든 시즌 조회", description = "지금까지 있었던 모든 시즌들을 조회한다")
    @GetMapping("/seasons")
    public List<Season> showAllSeasons() {
        return seasonRepository.findAll();
    }

    public enum Criteria {
        BEST_SCORE;
    }

    // TODO: 이하 개선 테스트 후 제거
    @Deprecated
    @Operation(summary = "전체 게임 점수 랭킹 조회", description = "게임 점수 기준으로 전체 랭킹을 50등까지 조회한다.")
    @GetMapping("/rankings/all")
    public List<RankingResponse> showRankings() {
        return legacyBestScoreRankingService.getEntireRankings();
    }

    @Deprecated
    @Operation(summary = "자신의 랭킹 조회", description = "게임 점수 기준으로 전체에서 자신의 랭킹을 조회한다.")
    @GetMapping("/rankings/all/me")
    public RankingResponse showBestRankingOf(@Authenticated Member member) {
        return legacyBestScoreRankingService.getBestRankingOf(member.getId());
    }
}
