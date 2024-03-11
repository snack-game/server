package com.snackgame.server.history.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.auth.token.support.Authenticated;
import com.snackgame.server.history.controller.dto.GameHistoryResponse;
import com.snackgame.server.history.dao.GameHistoryDao;
import com.snackgame.server.member.domain.Member;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GameHistoryController {

    private static final int GAME_RECORD_SIZE = 25;

    private final GameHistoryDao gameHistoryDao;

    @GetMapping("history/date/me")
    @Operation(summary = "자신의 게임 전적 조회", description = "최근 7일 동안의 최고 점수를 조회한다.")
    public List<GameHistoryResponse> showScoresByDate(@Authenticated Member member) {
        return gameHistoryDao.selectByDate(member.getId());
    }

    @GetMapping("history/session/me")
    @Operation(summary = "자신의 게임 전적 조회", description = "최근 25게임의 점수들을 조회한다.")
    public List<GameHistoryResponse> showScoresBySessions(@Authenticated Member member) {
        return gameHistoryDao.selectBySession(member.getId(), GAME_RECORD_SIZE);
    }
}
