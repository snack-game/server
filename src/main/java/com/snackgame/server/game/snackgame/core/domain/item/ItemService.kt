package com.snackgame.server.game.snackgame.core.domain.item

import com.snackgame.server.game.snackgame.core.service.dto.ItemCountResponse
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ItemService(private val itemRepository: ItemRepository) {

    fun checkItemPresence(ownerId: Long): ItemCountResponse {
        val items = itemRepository.findAllByOwnerId(ownerId)
        val counts = mutableMapOf<ItemType, Int>()

        ItemType.values().forEach { itemType ->
            val count = items.find { it.itemType == itemType }?.count ?: 0
            counts[itemType] = count
        }

        return ItemCountResponse.from(counts)
    }

    @Transactional
    fun useItem(ownerId: Long, itemType: ItemType) {
        val found = itemRepository.findItemByOwnerIdAndItemType(ownerId, itemType)
            .orElseThrow { IllegalStateException("사용자가 보유한 아이템이 없습니다") }
        if (found.count <= 0) {
            throw IllegalStateException("아이템이 부족합니다")
        }

        found.count -= 1
        itemRepository.save(found)
    }

}