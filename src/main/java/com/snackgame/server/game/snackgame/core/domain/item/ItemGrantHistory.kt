package com.snackgame.server.game.snackgame.core.domain.item

import com.snackgame.server.game.snackgame.core.domain.item.policy.GrantType
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "item_grant_history")
class ItemGrantHistory(
    val ownerId: Long,
    @Enumerated(EnumType.STRING)
    val itemType: ItemType,
    @Enumerated(EnumType.STRING)
    val grantType: GrantType,
    val grantedAt: LocalDateTime = LocalDateTime.now(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
)