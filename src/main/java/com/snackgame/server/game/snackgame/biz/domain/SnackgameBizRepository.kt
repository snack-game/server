package com.snackgame.server.game.snackgame.biz.domain

import com.snackgame.server.game.session.exception.NoSuchSessionException
import com.snackgame.server.game.snackgame.core.domain.Percentile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SnackgameBizRepository : JpaRepository<SnackgameBiz, Long> {

    fun findByOwnerIdAndSessionId(ownerId: Long, sessionId: Long): SnackgameBiz?

    @Query(
        value = """
            with scores as (
                select percent_rank() over (order by score desc) as percentile, session_id, score 
                from snackgame_biz where TIMESTAMPDIFF(SECOND, now(), expires_at) <=0
            )
            select percentile from scores where session_id = :sessionId""",
        nativeQuery = true
    )
    fun findPercentileOf(sessionId: Long): Double?

    fun deleteAllByOwnerId(memberId: Long)
}

fun SnackgameBizRepository.getBy(ownerId: Long, sessionId: Long): SnackgameBiz =
    findByOwnerIdAndSessionId(ownerId, sessionId) ?: throw NoSuchSessionException()

fun SnackgameBizRepository.ratePercentileOf(sessionId: Long): Percentile {
    with(findPercentileOf(sessionId)) {
        this ?: throw NoSuchSessionException()
        return Percentile(this)
    }
}