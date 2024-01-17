package com.snackgame.server.rank.applegame.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.auth.jwt.support.Authenticated;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.rank.applegame.AppleGameRankingService;
import com.snackgame.server.rank.applegame.controller.dto.RankResponseV2;
import com.snackgame.server.rank.applegame.controller.dto.RankingResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AppleGameRankingController {

    private final AppleGameRankingService appleGameRankingService;

    @Operation(summary = "전체 최고점수 랭킹 조회", description = "전체 랭킹을 50등까지 조회한다. 개인별 최고점수 기준이다.")
    @GetMapping("/rankings")
    public List<RankResponseV2> showRanksByBestScore(@RequestParam("by") Criteria criteria) {
        return appleGameRankingService.rank50ByBestScore();
    }

    @Operation(summary = "자신의 최고점수 랭킹 조회", description = "전체에서 자신의 랭킹을 조회한다. 개인별 최고점수 기준이다.")
    @GetMapping("/rankings/me")
    public RankResponseV2 showRankByBestScoreOf(@Authenticated Member member, @RequestParam("by") Criteria criteria) {
        return appleGameRankingService.rankByBestScoreOf(member.getId());
    }

    @Deprecated
    @Operation(summary = "전체 게임 점수 랭킹 조회", description = "게임 점수 기준으로 전체 랭킹을 50등까지 조회한다.")
    @GetMapping("/rankings/all")
    public List<RankingResponse> showRankings() {
        return appleGameRankingService.getEntireRankings();
    }

    @Deprecated
    @Operation(summary = "자신의 랭킹 조회", description = "게임 점수 기준으로 전체에서 자신의 랭킹을 조회한다.")
    @GetMapping("/rankings/all/me")
    public RankingResponse showBestRankingOf(@Authenticated Member member) {
        return appleGameRankingService.getBestRankingOf(member.getId());
    }

    private enum Criteria {
        BEST_SCORE
    }
}
