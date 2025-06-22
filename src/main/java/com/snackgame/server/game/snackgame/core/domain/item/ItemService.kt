package com.snackgame.server.game.snackgame.core.domain.item

import com.snackgame.server.game.snackgame.core.service.dto.ItemCountResponse
import com.snackgame.server.game.snackgame.core.service.dto.ItemTypeRequest
import org.springframework.stereotype.Service

@Service
class ItemService(private val itemRepository: ItemRepository) {

    fun checkItemPresence(ownerId : Long) : ItemCountResponse {
        val items = itemRepository.findAllByOwnerId(ownerId)
        val counts = mutableMapOf<ItemType, Int>()

        ItemType.values().forEach { itemType ->
            val count = items.find { it.itemType == itemType }?.count ?: 0
            counts[itemType] = count
        }

        return ItemCountResponse.from(counts)
    }

}