package com.snackgame.server.game.snackgame.core.domain

import com.snackgame.server.game.session.exception.NoSuchSessionException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SnackgameRepository : JpaRepository<Snackgame, Long> {

    fun findByOwnerIdAndSessionId(ownerId: Long, sessionId: Long): Snackgame?

    @Query(
        value = """
            with scores as (
                select percent_rank() over (order by score desc) as percentile, session_id, score 
                from snackgame where TIMESTAMPDIFF(SECOND, now(), expires_at) <=0
            )
            select percentile from scores where session_id = :sessionId""",
        nativeQuery = true
    )
    fun findPercentileOf(sessionId: Long): Double?

    @Modifying
    @Query("update Snackgame set ownerId = :toMemberId where ownerId = :fromMemberId")
    fun transferSessions(fromMemberId: Long, toMemberId: Long): Int

    fun deleteAllByOwnerId(memberId: Long)
}

fun SnackgameRepository.getBy(ownerId: Long, sessionId: Long): Snackgame =
    findByOwnerIdAndSessionId(ownerId, sessionId) ?: throw NoSuchSessionException()

fun SnackgameRepository.ratePercentileOf(sessionId: Long): Percentile {
    with(findPercentileOf(sessionId)) {
        this ?: throw NoSuchSessionException()
        return Percentile(this)
    }
}
