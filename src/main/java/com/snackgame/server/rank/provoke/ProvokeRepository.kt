package com.snackgame.server.rank.provoke

import org.springframework.data.jpa.repository.JpaRepository

interface ProvokeRepository : JpaRepository<Provoke, Long> {

    fun findProvokeByOwnerIdAndSessionId(ownerId: Long, sessionId: Long): Provoke
}
