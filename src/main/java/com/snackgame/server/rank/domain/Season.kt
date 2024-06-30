package com.snackgame.server.rank.domain

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Season(
    val name: String,
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime? = null,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
) {

    override fun toString(): String {
        return "Season(name='$name', startedAt=$startedAt, endedAt=$endedAt, id=$id)"
    }
}
