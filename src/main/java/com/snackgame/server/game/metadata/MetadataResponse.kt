package com.snackgame.server.game.metadata

import io.swagger.v3.oas.annotations.media.Schema

data class MetadataResponse(
    @field:Schema(example = "2")
    val id: Long,
    @field:Schema(example = "사과게임")
    val localizedName: String
) {

    companion object {
        fun of(metadata: Metadata): MetadataResponse =
            MetadataResponse(metadata.id, metadata.localizedName)
    }
}
