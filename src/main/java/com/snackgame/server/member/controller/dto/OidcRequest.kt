package com.snackgame.server.member.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Pattern

data class OidcRequest(
    @Schema(example = "header.payload.signature")
    @Pattern(regexp = "^[A-Za-z0-9_-]+\\.([A-Za-z0-9_-]+\\.?)+[A-Za-z0-9_-]+\$", message = "Id Token 형식이 틀렸습니다")
    val idToken: String
)
