package com.snackgame.server.rank.provoke

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProvokeRepository : JpaRepository<Provoke, Long> {

    fun findProvokeByOwnerIdAndSessionId(ownerId: Long, sessionId: Long): Optional<Provoke>
}
