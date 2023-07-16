package com.snackgame.server.applegame.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.applegame.business.AppleGameService;
import com.snackgame.server.applegame.business.domain.AppleGame;
import com.snackgame.server.applegame.controller.dto.AppleGameResponse;
import com.snackgame.server.applegame.controller.dto.MoveRequest;
import com.snackgame.server.member.business.domain.Member;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AppleGameController {

    private final AppleGameService appleGameService;

    @SecurityRequirement(name = "jwtAuth")
    @PostMapping("/games/1")
    public AppleGameResponse startGameFor(Member member) {
        AppleGame game = appleGameService.startGameOf(member);
        return AppleGameResponse.of(game);
    }

    @SecurityRequirement(name = "jwtAuth")
    @PutMapping("/sessions/{sessionId}/moves")
    public void placeMoves(Member member, @PathVariable Long sessionId, @RequestBody List<MoveRequest> moves) {
        appleGameService.placeMoves(member, sessionId, moves);
    }

    @SecurityRequirement(name = "jwtAuth")
    @DeleteMapping("/sessions/{sessionId}/board")
    public AppleGameResponse resetBoard(Member member, @PathVariable Long sessionId) {
        AppleGame game = appleGameService.resetBoard(member, sessionId);
        return AppleGameResponse.of(game);
    }

    @SecurityRequirement(name = "jwtAuth")
    @PutMapping("/sessions/{sessionId}/end")
    public void endSession(Member member, @PathVariable Long sessionId) {
        appleGameService.endSession(member, sessionId);
    }
}
