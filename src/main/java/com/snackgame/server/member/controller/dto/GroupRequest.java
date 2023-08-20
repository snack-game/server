package com.snackgame.server.member.controller.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GroupRequest {

    @NotBlank(message = "그룹 이름은 공백일 수 없습니다")
    private String group;
}
