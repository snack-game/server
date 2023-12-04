package com.snackgame.server.member.controller.dto;

import com.snackgame.server.member.domain.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberDetailsResponse {

    private final Long id;
    private final String name;
    @Schema(allowableValues = {"SELF", "GUEST", "SOCIAL"})
    private final String type;
    private final GroupResponse group;

    public static MemberDetailsResponse of(Member member) {
        return new MemberDetailsResponse(
                member.getId(),
                member.getNameAsString(),
                member.getAccountType().name(),
                GroupResponse.of(member.getGroup())
        );
    }
}
