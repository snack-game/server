package com.snackgame.server.member.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class NameRequest {

    @Schema(example = "홍길동")
    @NotBlank(message = "이름은 공백일 수 없습니다")
    @Size(min = 2, max = 13, message = "이름은 2글자 이상 13글자 이하여야 합니다")
    private String name;

    public String getName() {
        return name;
    }
}
