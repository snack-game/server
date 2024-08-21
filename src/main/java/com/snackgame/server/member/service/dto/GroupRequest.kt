package com.snackgame.server.member.service.dto

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.constraints.Pattern


data class GroupRequest @JsonCreator constructor(
    @Pattern(regexp = "[a-zA-Z가-힣0-9-_]{2,10}", message = "사용할 수 없는 그룹이름입니다")
    val group: String?
)
