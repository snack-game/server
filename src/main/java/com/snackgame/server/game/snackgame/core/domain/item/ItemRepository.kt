package com.snackgame.server.game.snackgame.core.domain.item

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ItemRepository : JpaRepository<Item, Long> {

    fun findAllByOwnerId(ownerId: Long): List<Item>
    fun findItemByOwnerIdAndItemType(ownerId: Long, itemType: ItemType) : Optional<Item>
}