package com.snackgame.server.game.snackgame.core.domain.item.policy

import com.snackgame.server.game.snackgame.exception.NoSuchPolicyException
import org.springframework.stereotype.Component

@Component
class GrantPolicySelector(
    private val daily: DailyGrantPolicy
) {
    private val map = mapOf(
        GrantType.DAILY to daily,
    )

    fun get(grantType: GrantType): ItemGrantPolicy {
        return map[grantType] ?: throw NoSuchPolicyException()
    }
}