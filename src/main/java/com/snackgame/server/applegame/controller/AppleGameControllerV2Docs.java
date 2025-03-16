package com.snackgame.server.applegame.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.snackgame.server.applegame.controller.dto.AppleGameResponseV2;
import com.snackgame.server.applegame.controller.dto.GameResultResponse;
import com.snackgame.server.applegame.controller.dto.RangeRequest;
import com.snackgame.server.member.domain.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "🍎 사과 게임")
public interface AppleGameControllerV2Docs {

    @Operation(summary = "게임 세션 시작", description = "1번 게임(사과게임) 세션을 시작한다")
    AppleGameResponseV2 startGameFor(Member member);

    @Operation(
            summary = "세션에 수 삽입",
            description = "지정한 세션에 수들을 삽입한다. 황금사과를 제거한 경우 초기화된 판을 응답한다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "판이 초기화되지 않은 경우"
                    ),
                    @ApiResponse(
                            responseCode = "201",
                            description = "황금사과로 판이 초기화된 경우",
                            content = @Content(examples = @ExampleObject(
                                    "{ \"sessionId\": 4, \"score\": 8, \"apples\": [ [ { \"number\": 3, \"golden\": true }, { \"number\": 1, \"golden\": false } ], [ { \"number\": 2, \"golden\": false }, { \"number\": 2, \"golden\": false } ] ] }"
                            ))
                    )
            }
    )
    ResponseEntity<AppleGameResponseV2> placeMoves(
            Member member,
            @PathVariable Long sessionId,
            @RequestBody List<RangeRequest> ranges
    );

    @Operation(summary = "게임 재시작", description = "게임을 재시작한다. 게임판과 시간이 초기화된다.")
    AppleGameResponseV2 restart(Member member, @PathVariable Long sessionId);

    @Operation(summary = "게임 세션 종료", description = "게임 세션을 종료한다. 응답에는 기록된 점수와 전체 게임에서의 백분위가 포함된다")
    GameResultResponse finish(Member member, @PathVariable Long sessionId);
}
