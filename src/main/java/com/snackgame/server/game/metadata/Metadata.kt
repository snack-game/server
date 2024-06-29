package com.snackgame.server.game.metadata

enum class Metadata(
    val gameId: Long,
    val localizedName: String
) {

    APPLE_GAME(1L, "사과게임"),
    SNACK_GAME(2L, "스낵게임")
}
