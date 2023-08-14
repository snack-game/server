package com.snackgame.server.member.controller.dto;

import com.snackgame.server.member.business.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberDetailsWithTokenResponse {

    private MemberDetailsResponse member;
    private String accessToken;

    public static MemberDetailsWithTokenResponse of(Member member, String accessToken) {
        return new MemberDetailsWithTokenResponse(
                MemberDetailsResponse.of(member),
                accessToken
        );
    }
}
