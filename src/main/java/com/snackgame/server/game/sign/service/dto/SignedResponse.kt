package com.snackgame.server.game.sign.service.dto

data class SignedResponse(
    val original: Any,
    val signed: String
)
