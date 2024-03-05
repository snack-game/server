package com.snackgame.server.member.controller.dto;

import com.snackgame.server.member.domain.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberDetailsResponse {

    @Schema(example = "1")
    private final Long id;
    @Schema(example = "닉네임")
    private final String name;
    private final GroupResponse group;
    private final String profileImage;
    private final StatusResponse status;
    @Schema(example = "SOCIAL", allowableValues = {"SELF", "GUEST", "SOCIAL"})
    private final String type;

    public static MemberDetailsResponse of(Member member) {
        return new MemberDetailsResponse(
                member.getId(),
                member.getNameAsString(),
                GroupResponse.of(member.getGroup()),
                member.getProfileImage().getUrl(),
                StatusResponse.of(member.getStatus()),
                member.getAccountType().name()
        );
    }
}
