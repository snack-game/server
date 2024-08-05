package com.snackgame.server.member.service.dto

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.constraints.NotBlank


data class GroupRequest @JsonCreator constructor(
    @field:NotBlank(message = "그룹 이름은 공백일 수 없습니다")
    val group: String
)
