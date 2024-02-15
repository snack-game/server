package com.snackgame.server.rank.applegame.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.auth.token.support.Authenticated;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.rank.applegame.BestScoreRankingService;
import com.snackgame.server.rank.applegame.controller.dto.RankResponseV2;
import com.snackgame.server.rank.applegame.controller.dto.RankingResponse;
import com.snackgame.server.rank.applegame.domain.Season;
import com.snackgame.server.rank.applegame.domain.SeasonRepository;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AppleGameRankingController {

    private final BestScoreRankingService bestScoreRankingService;
    private final SeasonRepository seasonRepository;

    @Operation(summary = "전체 시즌 최고점수 랭크 조회", description = "전체 시즌에서 최고점수 랭킹을 50등까지 조회한다")
    @GetMapping("/rankings")
    public List<RankResponseV2> showRanksByBestScore(@RequestParam("by") Criteria criteria) {
        return bestScoreRankingService.rankLeadingBestScores();
    }

    @Operation(summary = "전체 시즌 자신의 최고점수 랭크 조회", description = "전체 시즌에서 자신의 최고점수 랭킹을 조회한다")
    @GetMapping("/rankings/me")
    public RankResponseV2 showRankByBestScoreOf(@Authenticated Member member, @RequestParam("by") Criteria criteria) {
        return bestScoreRankingService.rankBestScoreOf(member.getId());
    }

    @Operation(summary = "최고점수 랭크 조회", description = "특정 시즌에서 최고점수 랭킹을 50등까지 조회한다")
    @GetMapping("/rankings/{seasonId}")
    public List<RankResponseV2> showRanksByBestScore(
            @PathVariable Long seasonId,
            @RequestParam("by") Criteria criteria
    ) {
        return bestScoreRankingService.rankLeadingBestScoresBy(seasonId);
    }

    @Operation(summary = "자신의 최고점수 랭크 조회", description = "특정 시즌에서 자신의 최고점수 랭킹을 조회한다")
    @GetMapping("/rankings/{seasonId}/me")
    public RankResponseV2 showRankByBestScoreOf(
            @Authenticated Member member,
            @PathVariable Long seasonId,
            @RequestParam("by") Criteria criteria
    ) {
        return bestScoreRankingService.rankBestScoreOf(member.getId(), seasonId);
    }

    @Operation(summary = "모든 시즌 조회", description = "지금까지 있었던 모든 시즌들을 조회한다")
    @GetMapping("/seasons")
    public List<Season> showAllSeasons() {
        return seasonRepository.findAll();
    }

    private enum Criteria {
        BEST_SCORE;
    }

    // TODO: 이하 개선 테스트 후 제거
    @Deprecated
    @Operation(summary = "전체 게임 점수 랭킹 조회", description = "게임 점수 기준으로 전체 랭킹을 50등까지 조회한다.")
    @GetMapping("/rankings/all")
    public List<RankingResponse> showRankings() {
        return bestScoreRankingService.getEntireRankings();
    }

    @Deprecated
    @Operation(summary = "자신의 랭킹 조회", description = "게임 점수 기준으로 전체에서 자신의 랭킹을 조회한다.")
    @GetMapping("/rankings/all/me")
    public RankingResponse showBestRankingOf(@Authenticated Member member) {
        return bestScoreRankingService.getBestRankingOf(member.getId());
    }
}
