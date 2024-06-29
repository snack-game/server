package com.snackgame.server.rank.controller.dto;

import com.snackgame.server.member.controller.dto.GroupResponse;
import com.snackgame.server.member.controller.dto.StatusResponse;
import com.snackgame.server.rank.domain.BestScoreWithRankAndOwner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RankResponseV2 {

    @Schema(example = "9")
    private final long rank;
    @Schema(example = "110")
    private final int score;
    private final RankOwnerResponse owner;
    @Schema(example = "1")
    private final long seasonId;

    public static RankResponseV2 of(BestScoreWithRankAndOwner bestScore) {
        return new RankResponseV2(
                bestScore.getRank(),
                bestScore.getScore(),
                new RankOwnerResponse(
                        bestScore.getOwnerId(),
                        bestScore.getOwnerName(),
                        bestScore.getOwnerProfileImage(),
                        new StatusResponse(
                                bestScore.getOwnerLevel(),
                                null,
                                null
                        ),
                        new GroupResponse(
                                bestScore.getOwnerGroupId(),
                                bestScore.getOwnerGroupName()
                        )
                ),
                bestScore.getSeasonId()
        );
    }
}
