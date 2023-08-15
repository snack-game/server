package com.snackgame.server.applegame.controller.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankingsResponse {

    private final List<RankingResponse> rankings;
    private final RankingResponse me;
}
