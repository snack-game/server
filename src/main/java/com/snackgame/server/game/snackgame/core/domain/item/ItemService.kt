package com.snackgame.server.game.snackgame.core.domain.item


import com.snackgame.server.game.snackgame.core.domain.item.policy.GrantPolicySelector
import com.snackgame.server.game.snackgame.core.domain.item.policy.GrantType
import com.snackgame.server.game.snackgame.core.service.dto.ItemResponse
import com.snackgame.server.game.snackgame.core.service.dto.ItemsResponse
import com.snackgame.server.game.snackgame.exception.ItemNotReadyException
import com.snackgame.server.game.snackgame.exception.NoItemException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

import javax.transaction.Transactional

@Service
class ItemService(
    private val itemRepository: ItemRepository,
    private val grantPolicySelector: GrantPolicySelector,
    private val itemGrantHistories: ItemGrantHistories
) {


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
            .orElseThrow { NoItemException() }

        found.useOne()
        itemRepository.save(found)
    }
    
    @Transactional
    fun issueItem(ownerId: Long, itemType: ItemType) : ItemResponse {
        val found = itemRepository.findItemByOwnerIdAndItemType(ownerId, itemType)
            .orElse(Item(ownerId = ownerId, itemType = itemType, count = 0, LocalDateTime.now()))

        found.count += 1
        itemRepository.save(found)
        return ItemResponse.of(found)
    }


    @Transactional
    fun issueItem(ownerId: Long, itemType: ItemType, grantType: GrantType): ItemResponse {
        val histories = itemGrantHistories.findAllByOwnerIdAndItemType(ownerId, itemType)
        val policy = grantPolicySelector.get(grantType)
        if (!policy.canGrant(ownerId, itemType, histories)) {
            throw ItemNotReadyException()
        }

        val found = itemRepository.findItemByOwnerIdAndItemType(ownerId, itemType)
            .orElse(Item(ownerId = ownerId, itemType = itemType, count = 0, LocalDateTime.now()))

        itemGrantHistories.save(ItemGrantHistory(ownerId, itemType, grantType))
        found.addOne()
        return ItemResponse.of(itemRepository.save(found))
    }

}