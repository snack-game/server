package com.snackgame.server.member.service.dto

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank

data class MemberRequest(
    @field:Schema(example = "홍길동")
    @field:NotBlank(message = "이름은 공백일 수 없습니다")
    val name: String,
    @field:Schema(example = "숭실대학교")
    val group: String
)
