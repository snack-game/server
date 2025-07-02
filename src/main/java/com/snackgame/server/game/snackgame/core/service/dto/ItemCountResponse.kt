package com.snackgame.server.game.snackgame.core.service.dto

import com.snackgame.server.game.snackgame.core.domain.item.ItemType

data class ItemCountResponse(
    val items: Map<ItemType, Int>
) {
    companion object {
        fun from(counts: Map<ItemType, Int>): ItemCountResponse {
            return ItemCountResponse(counts)
        }
    }
}