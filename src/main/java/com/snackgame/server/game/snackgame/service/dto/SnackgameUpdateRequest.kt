package com.snackgame.server.game.snackgame.service.dto

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Positive

data class SnackgameUpdateRequest(
    @Positive(message = "점수는 음수일 수 없습니다")
    @field:Schema(example = "10")
    val score: Int
)
