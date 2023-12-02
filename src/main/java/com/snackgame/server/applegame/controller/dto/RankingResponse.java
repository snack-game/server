package com.snackgame.server.applegame.controller.dto;

import com.snackgame.server.applegame.business.domain.game.AppleGame;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Deprecated
public class RankingResponse {

    private final int ranking;
    private final Long ownerId;
    private final int score;

    public static RankingResponse of(int ranking, AppleGame appleGame) {
        return new RankingResponse(
                ranking,
                appleGame.getOwnerId(),
                appleGame.getScore()
        );
    }
}
