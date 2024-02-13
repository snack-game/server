package com.snackgame.server.member.controller.dto;

import com.snackgame.server.member.domain.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberDetailsWithTokenResponse {

    private MemberDetailsResponse member;
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1NTQiLCJ3MTU0N30.QsyZ")
    private String accessToken;

    public static MemberDetailsWithTokenResponse of(Member member, String accessToken) {
        return new MemberDetailsWithTokenResponse(
                MemberDetailsResponse.of(member),
                accessToken
        );
    }
}
