package com.snackgame.server.game.snackgame.core.service.dto

import com.snackgame.server.game.snackgame.core.domain.item.Item
import com.snackgame.server.game.snackgame.core.domain.item.ItemType
import java.time.LocalDateTime

data class ItemResponse(
    val ownerId: Long,
    val type: ItemType,
    val count: Int,
    val lastGrantedAt: LocalDateTime? = null,
) {
    companion object {
        fun of(item: Item): ItemResponse {
            return ItemResponse(
                item.ownerId,
                item.itemType,
                item.count,
                item.lastGrantedAt,
            )
        }
    }
}