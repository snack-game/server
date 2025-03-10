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

import com.snackgame.server.applegame.controller.dto.AppleGameResponseV2;
import com.snackgame.server.applegame.controller.dto.GameResultResponse;
import com.snackgame.server.applegame.controller.dto.RangeRequest;
import com.snackgame.server.applegame.domain.game.AppleGame;
import com.snackgame.server.applegame.service.AppleGameService;
import com.snackgame.server.auth.token.support.Authenticated;
import com.snackgame.server.member.domain.Member;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2")
public class AppleGameControllerV2 implements AppleGameControllerV2Docs {

    private final AppleGameService appleGameService;

    @Override
    @PostMapping("/games/1")
    public AppleGameResponseV2 startGameFor(@Authenticated Member member) {
        AppleGame game = appleGameService.startGameFor(member.getId());
        return AppleGameResponseV2.of(game);
    }

    @Override
    @PutMapping("/sessions/{sessionId}/moves")
    public ResponseEntity<AppleGameResponseV2> placeMoves(
            @Authenticated Member member,
            @PathVariable Long sessionId,
            @RequestBody List<RangeRequest> ranges
    ) {
        return appleGameService.placeMoves(member.getId(), sessionId, ranges)
                .map(game -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(AppleGameResponseV2.of(game))
                )
                .orElseGet(() -> ResponseEntity.ok().build());
    }

    @PostMapping("/sessions/{sessionId}/score")
    public void increaseScore(
            @Authenticated Member member,
            @PathVariable Long sessionId,
            int amount
    ) {
        appleGameService.increaseScore(member.getId(), sessionId, amount);
    }

    @Override
    @DeleteMapping("/sessions/{sessionId}/board")
    public AppleGameResponseV2 restart(@Authenticated Member member, @PathVariable Long sessionId) {
        AppleGame game = appleGameService.restart(member.getId(), sessionId);
        return AppleGameResponseV2.of(game);
    }

    @Override
    @PutMapping("/sessions/{sessionId}/end")
    public GameResultResponse finish(@Authenticated Member member, @PathVariable Long sessionId) {
        return appleGameService.finish(member.getId(), sessionId);
    }
}
