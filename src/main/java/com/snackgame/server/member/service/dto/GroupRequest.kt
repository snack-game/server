package com.snackgame.server.member.service.dto

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.constraints.Size


data class GroupRequest @JsonCreator constructor(
    @Size(min = 1, message = "그룹 이름이 너무 짧습니다")
    val group: String?
)
