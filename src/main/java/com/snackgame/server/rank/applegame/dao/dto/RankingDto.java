package com.snackgame.server.rank.applegame.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Deprecated
public class RankingDto {

    private final int ranking;
    private final Long sessionId;
}
