package com.snackgame.server.member.service.dto

import com.fasterxml.jackson.annotation.JsonCreator


data class GroupRequest @JsonCreator constructor(
    val group: String
)
