package com.snackgame.server.member.controller.dto;

import com.snackgame.server.member.business.domain.Member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberDetailsResponse {

    private final Long id;
    private final String name;
    private final GroupResponse group;

    public static MemberDetailsResponse of(Member member) {
        return new MemberDetailsResponse(
                member.getId(),
                member.getNameAsString(),
                GroupResponse.of(member.getGroup())
        );
    }
}
