package com.snackgame.server.member.controller.dto;

import com.snackgame.server.member.business.domain.Member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberDetailsWithTokenResponse {

    private final Long id;
    private final String name;
    private final GroupResponse group;
    private final String accessToken;

    public static MemberDetailsWithTokenResponse of(Member member, String accessToken) {
        return new MemberDetailsWithTokenResponse(
                member.getId(),
                member.getName(),
                GroupResponse.of(member.getGroup()),
                accessToken
        );
    }
}
