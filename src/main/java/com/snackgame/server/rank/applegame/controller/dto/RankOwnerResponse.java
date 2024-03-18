package com.snackgame.server.rank.applegame.controller.dto;

import com.snackgame.server.member.controller.dto.GroupResponse;
import com.snackgame.server.member.domain.Member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RankOwnerResponse {

    private final Long id;
    private final String name;
    private final Long level;
    private final GroupResponse group;

    public static RankOwnerResponse of(Member member) {
        return new RankOwnerResponse(
                member.getId(),
                member.getNameAsString(),
                member.getStatus().getLevel(),
                GroupResponse.of(member.getGroup())
        );
    }
}
