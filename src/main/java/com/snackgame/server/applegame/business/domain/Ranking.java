package com.snackgame.server.applegame.business.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Ranking {

    private final int ranking;
    private final AppleGame session;
}
