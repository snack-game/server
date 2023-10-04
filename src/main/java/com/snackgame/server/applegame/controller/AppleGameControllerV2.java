package com.snackgame.server.applegame.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.applegame.business.AppleGameService;
import com.snackgame.server.applegame.business.domain.AppleGame;
import com.snackgame.server.applegame.controller.dto.AppleGameResponseV2;
import com.snackgame.server.applegame.controller.dto.MoveRequest;
import com.snackgame.server.member.business.domain.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2")
public class AppleGameControllerV2 {

    private final AppleGameService appleGameService;

    @Operation(summary = "게임 세션 시작", description = "1번 게임(사과게임) 세션을 시작한다")
    @PostMapping("/games/1")
    public AppleGameResponseV2 startGameFor(Member member) {
        AppleGame game = appleGameService.startGameOf(member);
        return AppleGameResponseV2.of(game);
    }

    @Operation(summary = "세션에 수 삽입", description = "지정한 세션에 수들을 삽입한다. 황금사과를 제거한 경우 초기화된 판을 응답한다.", responses = {
            @ApiResponse(responseCode = "200", description = "판이 초기화되지 않은 경우", content = @Content()),
            @ApiResponse(responseCode = "205", description = "황금사과로 판이 초기화된 경우")
    })
    @PutMapping("/sessions/{sessionId}/moves")
    public ResponseEntity<AppleGameResponseV2> placeMoves(
            Member member,
            @PathVariable Long sessionId,
            @RequestBody List<MoveRequest> moves
    ) {
        return appleGameService.placeMoves(member, sessionId, moves)
                .map(game -> ResponseEntity
                        .status(HttpStatus.RESET_CONTENT)
                        .body(AppleGameResponseV2.of(game))
                )
                .orElseGet(() -> ResponseEntity.ok().build());
    }

    @Operation(summary = "게임판 초기화", description = "지정한 세션의 게임판을 초기화한다. 황금사과와는 별도의 기능이다.")
    @DeleteMapping("/sessions/{sessionId}/board")
    public AppleGameResponseV2 resetBoard(Member member, @PathVariable Long sessionId) {
        AppleGame game = appleGameService.resetBoard(member, sessionId);
        return AppleGameResponseV2.of(game);
    }

    @Operation(summary = "게임 세션 종료", description = "현재 세션의 종료를 알린다")
    @PutMapping("/sessions/{sessionId}/end")
    public void endSession(Member member, @PathVariable Long sessionId) {
        appleGameService.endSession(member, sessionId);
    }
}
