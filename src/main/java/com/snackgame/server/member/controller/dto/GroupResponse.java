package com.snackgame.server.member.controller.dto;

import java.util.Objects;

import com.snackgame.server.member.domain.Group;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GroupResponse {

    @Schema(example = "2")
    private final Long id;
    @Schema(example = "숭실대학교")
    private final String name;

    public static GroupResponse of(Group group) {
        if (Objects.isNull(group)) {
            return null;
        }
        return new GroupResponse(
                group.getId(),
                group.getName()
        );
    }
}
