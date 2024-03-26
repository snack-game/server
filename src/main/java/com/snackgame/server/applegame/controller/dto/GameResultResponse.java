package com.snackgame.server.applegame.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameResultResponse {

    @Schema(example = "117")
    private final int score;
    @Schema(example = "5")
    private final int percentile;
}
