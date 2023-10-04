package com.snackgame.server.applegame.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.snackgame.server.applegame.business.domain.Apple;
import com.snackgame.server.applegame.business.domain.AppleGame;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppleGameResponseV2 {

    private final Long sessionId;
    private final Integer score;
    private final List<List<AppleResponse>> apples;

    public static AppleGameResponseV2 of(AppleGame appleGame) {
        return new AppleGameResponseV2(
                appleGame.getSessionId(),
                appleGame.getScore(),
                toResponses(appleGame.getApples())
        );
    }

    private static List<List<AppleResponse>> toResponses(List<List<Apple>> apples) {
        return apples.stream()
                .map(AppleResponse::of)
                .collect(Collectors.toList());
    }
}
