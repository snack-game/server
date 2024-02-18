package com.snackgame.server.applegame.controller.dto;

import java.util.List;

import com.snackgame.server.applegame.domain.game.AppleGame;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppleGameResponseV2 {

    private final Long sessionId;
    private final Double score;
    private final List<List<AppleResponse>> apples;

    public static AppleGameResponseV2 of(AppleGame appleGame) {
        return new AppleGameResponseV2(
                appleGame.getSessionId(),
                appleGame.getScore(),
                AppleResponse.of(appleGame.getApples())
        );
    }
}
