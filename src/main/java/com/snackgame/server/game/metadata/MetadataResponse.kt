package com.snackgame.server.game.metadata

import io.swagger.v3.oas.annotations.media.Schema

data class MetadataResponse(
    @field:Schema(example = "2")
    val gameId: Long,
    @field:Schema(example = "스낵게임")
    val localizedName: String
) {

    companion object {
        fun of(metadata: Metadata): MetadataResponse =
            MetadataResponse(metadata.gameId, metadata.localizedName)
    }
}
