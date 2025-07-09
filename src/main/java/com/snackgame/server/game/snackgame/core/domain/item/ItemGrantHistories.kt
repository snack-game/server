package com.snackgame.server.game.snackgame.core.domain.item

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemGrantHistories : JpaRepository<ItemGrantHistory, Long> {
    fun findAllByOwnerIdAndItemType(ownerId: Long, itemType: ItemType): List<ItemGrantHistory>
}