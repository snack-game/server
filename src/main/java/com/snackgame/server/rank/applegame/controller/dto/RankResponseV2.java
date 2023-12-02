package com.snackgame.server.rank.applegame.controller.dto;

import com.snackgame.server.member.controller.dto.GroupResponse;
import com.snackgame.server.rank.applegame.domain.BestScoreWithRankAndOwner;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RankResponseV2 {

    private final long rank;
    private final int score;
    private final RankOwnerResponse owner;

    public static RankResponseV2 of(BestScoreWithRankAndOwner bestScore) {
        return new RankResponseV2(
                bestScore.getRank(),
                bestScore.getScore(),
                new RankOwnerResponse(
                        bestScore.getOwnerId(),
                        bestScore.getOwnerName(),
                        new GroupResponse(
                                bestScore.getOwnerGroupId(),
                                bestScore.getOwnerGroupName()
                        )
                )
        );
    }
}
