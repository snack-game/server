package com.snackgame.server.game.snackgame.core.domain.item.policy

import com.snackgame.server.game.snackgame.core.domain.item.ItemGrantHistory
import com.snackgame.server.game.snackgame.core.domain.item.ItemType

interface ItemGrantPolicy {
    fun canGrant(ownerId: Long, itemType: ItemType, histories: List<ItemGrantHistory>): Boolean
}