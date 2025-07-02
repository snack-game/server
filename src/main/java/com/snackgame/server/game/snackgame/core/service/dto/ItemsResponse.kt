package com.snackgame.server.game.snackgame.core.service.dto

import com.snackgame.server.game.snackgame.core.domain.item.Item
import com.snackgame.server.game.snackgame.core.domain.item.ItemType

data class ItemsResponse(
    val items: List<ItemResponse>
) {
    companion object {
        fun from(items: List<Item>): ItemsResponse {
            return ItemsResponse(
                items.map { ItemResponse.of(it) }
            )
        }
    }
}