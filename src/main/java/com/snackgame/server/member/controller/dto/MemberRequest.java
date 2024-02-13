package com.snackgame.server.member.controller.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberRequest {

    @Schema(example = "홍길동")
    @NotBlank(message = "이름은 공백일 수 없습니다")
    private String name;
    @Schema(example = "숭실대학교")
    private String group;
}
