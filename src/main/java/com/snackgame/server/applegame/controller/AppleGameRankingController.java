package com.snackgame.server.applegame.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.applegame.business.AppleGameRankingService;
import com.snackgame.server.applegame.controller.dto.RankingResponse;
import com.snackgame.server.auth.jwt.FromToken;
import com.snackgame.server.member.business.domain.Member;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AppleGameRankingController {

    private final AppleGameRankingService appleGameRankingService;

    @Operation(summary = "전체 랭킹 조회", description = "전체 랭킹을 50등까지 조회한다. 최대 점수 기준이다.")
    @GetMapping("/rankings/all")
    public List<RankingResponse> showRankings() {
        return appleGameRankingService.getEntireRankings();
    }

    @Operation(summary = "자신의 랭킹 조회", description = "전체에서 자신의 랭킹을 조회한다. 최대 점수 기준이다.")
    @GetMapping("/rankings/all/me")
    public RankingResponse showBestRankingOf(@FromToken Member member) {
        return appleGameRankingService.getBestRankingOf(member.getId());
    }
}
