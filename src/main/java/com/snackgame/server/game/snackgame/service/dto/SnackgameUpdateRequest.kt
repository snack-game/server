package com.snackgame.server.game.snackgame.service.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.beans.ConstructorProperties
import javax.validation.constraints.Positive

data class SnackgameUpdateRequest @ConstructorProperties("score") constructor(
    @Positive(message = "점수는 양수여야 합니다")
    @field:Schema(example = "10")
    val score: Int
)
