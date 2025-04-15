package com.snackgame.server.rank.provoke

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "provoke")
class Provoke(
    val ownerId: Long,
    val sessionId: Long,
    var updated: Boolean = false,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
) {

    fun getReady() {
        this.updated = true
    }

    fun isUpdated(): Boolean {
        return this.updated
    }
}
