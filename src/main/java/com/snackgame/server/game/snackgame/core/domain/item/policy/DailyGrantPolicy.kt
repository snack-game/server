package com.snackgame.server.game.snackgame.core.domain.item.policy

import com.snackgame.server.game.snackgame.core.domain.item.ItemGrantHistory
import com.snackgame.server.game.snackgame.core.domain.item.ItemType
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DailyGrantPolicy : ItemGrantPolicy {
    override fun canGrant(
        ownerId: Long,
        itemType: ItemType,
        histories: List<ItemGrantHistory>
    ): Boolean {
        val latest = histories.filter { it.grantType == GrantType.DAILY }
            .maxByOrNull { it.grantedAt } ?: return true
        return latest.grantedAt.plusDays(1).isBefore(LocalDateTime.now())
    }

}