package com.snackgame.server.member.controller.dto;

import com.snackgame.server.member.business.domain.Group;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GroupResponse {

    private final Long id;
    private final String name;

    public static GroupResponse of(Group group) {
        return new GroupResponse(
                group.getId(),
                group.getName()
        );
    }
}
