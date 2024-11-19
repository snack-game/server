package com.snackgame.server.game.snackgame.biz.domain

import com.snackgame.server.game.session.exception.NoSuchSessionException
import com.snackgame.server.game.snackgame.core.domain.Percentile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SnackgameBizV2Repository : JpaRepository<SnackgameBizV2, Long> {
    fun findByOwnerIdAndSessionId(ownerId: Long, sessionId: Long): SnackgameBizV2?

    @Query(
        value = """
            with scores as (
                select percent_rank() over (order by score desc) as percentile, session_id, score 
                from snackgame_biz_v2 where TIMESTAMPDIFF(SECOND, now(), expires_at) <=0
            )
            select percentile from scores where session_id = :sessionId""",
        nativeQuery = true
    )
    fun findPercentileOf(sessionId: Long): Double?
}

fun SnackgameBizV2Repository.getBy(ownerId: Long, sessionId: Long): SnackgameBizV2 =
    findByOwnerIdAndSessionId(ownerId, sessionId) ?: throw NoSuchSessionException()

fun SnackgameBizV2Repository.ratePercentileOf(sessionId: Long): Percentile {
    with(findPercentileOf(sessionId)) {
        this ?: throw NoSuchSessionException()
        return Percentile(this)
    }
}
