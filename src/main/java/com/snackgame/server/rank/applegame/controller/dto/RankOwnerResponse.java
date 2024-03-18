package com.snackgame.server.rank.applegame.controller.dto;

import com.snackgame.server.member.controller.dto.GroupResponse;
import com.snackgame.server.member.controller.dto.StatusResponse;
import com.snackgame.server.member.domain.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RankOwnerResponse {

    @Schema(example = "1")
    private final Long id;
    @Schema(example = "보리")
    private final String name;
    @Schema(example = "https://snackgame.s3.ap-northeast-2.amazonaws.com/unhashed/7d9b26272791438b8dc6893a4cbd6f50-77423374")
    private final String profileImage;
    private final StatusResponse status;
    private final GroupResponse group;

    // TODO: MemberResponse와 그 형태를 동일하게 하기 위해 제거한다
    private final Long level;

    public RankOwnerResponse(Long id, String name, String profileImage, StatusResponse status, GroupResponse group) {
        this.id = id;
        this.name = name;
        this.profileImage = profileImage;
        this.status = status;
        this.group = group;

        // TODO: 제거
        this.level = status.getLevel();
    }

    // TODO: 사용자를 받아와 응답을 만든다
    public static RankOwnerResponse of(Member member) {
        return new RankOwnerResponse(
                member.getId(),
                member.getNameAsString(),
                member.getProfileImage().getUrl(),
                StatusResponse.of(member.getStatus()),
                GroupResponse.of(member.getGroup())
        );
    }
}
