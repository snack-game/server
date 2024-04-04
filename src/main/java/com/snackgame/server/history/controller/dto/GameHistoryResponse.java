package com.snackgame.server.history.controller.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameHistoryResponse {

    private final long session_id;

    private final long memberId;

    private final int score;

    private final LocalDate updatedAt;
}
