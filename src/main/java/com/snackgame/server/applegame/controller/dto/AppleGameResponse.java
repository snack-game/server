package com.snackgame.server.applegame.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.snackgame.server.applegame.business.domain.Apple;
import com.snackgame.server.applegame.business.domain.AppleGame;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppleGameResponse {

    private final Long sessionId;
    private final Integer score;
    private final List<List<Integer>> apples;

    public static AppleGameResponse of(AppleGame appleGame) {
        return new AppleGameResponse(
                appleGame.getSessionId(),
                appleGame.getScore(),
                toIntegers(appleGame.getApples())
        );
    }

    private static List<List<Integer>> toIntegers(List<List<Apple>> apples) {
        return apples.stream()
                .map(row -> row.stream()
                        .map(Apple::getNumber)
                        .collect(Collectors.toList())
                )
                .collect(Collectors.toList());
    }
}
