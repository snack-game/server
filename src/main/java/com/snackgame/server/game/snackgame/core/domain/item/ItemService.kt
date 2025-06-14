package com.snackgame.server.game.snackgame.core.domain.item

import com.snackgame.server.game.snackgame.core.service.dto.ItemTypeRequest
import org.springframework.stereotype.Service

@Service
class ItemService(private val itemRepository: ItemRepository) {

    fun checkItemPresence(ownerId: Long, itemTypeRequest: ItemTypeRequest): Boolean {
        val found = itemRepository.findItemByOwnerIdAndItemType(ownerId, itemTypeRequest.itemType)
        return found.count > 0
    }

}