package com.snackgame.server.history.controller.dto

import java.time.LocalDate

data class GameHistoryResponse(
    val sessionId: Long,
    val memberId: Long,
    val score: Int,
    val updatedAt: LocalDate
)
