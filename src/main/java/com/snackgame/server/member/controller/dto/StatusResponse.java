package com.snackgame.server.member.controller.dto;

import com.snackgame.server.member.domain.Status;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class StatusResponse {

    @Schema(example = "1")
    private final Long level;
    @Schema(example = "200")
    private final Double exp;
    @Schema(example = "240")
    private final Double maxExp;

    public static StatusResponse of(Status status) {
        return new StatusResponse(
                status.getLevel(),
                status.getExp().doubleValue(),
                status.expRequiredForLevel().doubleValue()
        );
    }
}
