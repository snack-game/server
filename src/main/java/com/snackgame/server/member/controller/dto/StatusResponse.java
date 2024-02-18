package com.snackgame.server.member.controller.dto;

import com.snackgame.server.member.domain.Status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class StatusResponse {

    private final Long level;

    private final Double exp;

    public static StatusResponse of(Status status) {
        return new StatusResponse(
                status.getLevel(),
                status.getExp());
    }
}
