package com.snackgame.server.applegame.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankingDto {

    private final int ranking;
    private final Long sessionId;
}
