package com.snackgame.server.game.snackgame.domain;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SnackgameInifiniteRepository : JpaRepository<SnackgameInfinite, Long> {

    fun findByOwnerIdAndSessionId(ownerId: Long, sessionId: Long): SnackgameInfinite?

    @Query(
        value = """
            with scores as (
                select percent_rank() over (order by score desc) as percentile, session_id, score 
                from snackgame_infinite where TIMESTAMPDIFF(SECOND, now(), expires_at) <=0
            )
            select percentile from scores where session_id = :sessionId""",
        nativeQuery = true
    )
    fun findPercentileOf(sessionId: Long): Double?
}
