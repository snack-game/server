package com.snackgame.server.game.snackgame.core.domain.item

import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long> {

    fun findAllByOwnerId(ownerId: Long): List<Item>
}