package com.snackgame.server.game.snackgame.core.domain.item

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ItemRepository : JpaRepository<Item, Long> {

    fun findAllByOwnerId(ownerId: Long): List<Item>
    fun findItemByOwnerIdAndItemType(ownerId: Long, itemType: ItemType): Optional<Item>
}