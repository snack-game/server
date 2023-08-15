package com.snackgame.server.applegame.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.applegame.business.AppleGameRankingService;
import com.snackgame.server.applegame.controller.dto.RankingResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AppleGameRankingController {

    private final AppleGameRankingService appleGameRankingService;

    @Operation(summary = "전체 랭킹 확인", description = "전체 랭킹을 50등까지 점수 순으로 가져온다")
    @GetMapping("/rankings/all")
    public List<RankingResponse> showEntireRankings() {
        return appleGameRankingService.getEntireRankings();
    }
}
