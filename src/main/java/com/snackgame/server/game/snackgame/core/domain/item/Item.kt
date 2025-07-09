package com.snackgame.server.game.snackgame.core.domain.item

import com.snackgame.server.game.snackgame.exception.NegativeItemCountException
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "item", uniqueConstraints = [UniqueConstraint(columnNames = ["ownerId", "itemType"])])
class Item(
    val ownerId: Long,
    @Enumerated(EnumType.STRING)
    val itemType: ItemType,
    var count: Int = 0,
    var lastGrantedAt: LocalDateTime = LocalDateTime.now(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
) {
    fun addOne() {
        this.count += 1
        lastGrantedAt = LocalDateTime.now()
    }

    fun useOne() {
        if (!validatePositive()) {
            throw NegativeItemCountException()
        }
        this.count -= 1
    }

    private fun validatePositive(): Boolean {
        return this.count > 0
    }
}