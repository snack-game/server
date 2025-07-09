package com.snackgame.server.game.snackgame.core.domain.item

import com.snackgame.server.game.snackgame.core.service.dto.ItemResponse
import com.snackgame.server.game.snackgame.core.service.dto.ItemsResponse
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class ItemService(private val itemRepository: ItemRepository) {

    @Transactional
    fun checkItemPresence(ownerId: Long): ItemsResponse {
        val items = itemRepository.findAllByOwnerId(ownerId)

        if (items.isEmpty()) {
            val newItems = ItemType.values().map { type ->
                Item(ownerId = ownerId, itemType = type)
            }
            return ItemsResponse.from(itemRepository.saveAll(newItems))
        }
        return ItemsResponse.from(items)
    }


    @Transactional
    fun useItem(ownerId: Long, itemType: ItemType) {
        val found = itemRepository.findItemByOwnerIdAndItemType(ownerId, itemType)
            .orElseThrow { IllegalStateException("사용자가 보유한 아이템이 없습니다") }
        if (found.count <= 0) {
            throw IllegalStateException("아이템이 부족합니다")
        }

        found.removeCount()
        itemRepository.save(found)
    }

    @Transactional
    fun issueItem(ownerId: Long, itemType: ItemType): ItemResponse {
        val found = itemRepository.findItemByOwnerIdAndItemType(ownerId, itemType)
            .orElse(Item(ownerId = ownerId, itemType = itemType, count = 0, LocalDateTime.now()))

        found.addCount()
        return ItemResponse.of(itemRepository.save(found))
    }

}