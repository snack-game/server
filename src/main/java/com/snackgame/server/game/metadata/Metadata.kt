package com.snackgame.server.game.metadata

enum class Metadata(
    val gameId: Long,
    val localizedName: String
) {

    APPLE_GAME(1, "사과게임"),
    SNACK_GAME(2, "스낵게임"),
    SNACK_GAME_INFINITE(3, "스낵게임 무한모드"),
    SNACK_GAME_BIZ(4, "스낵게임 Biz"),
}
