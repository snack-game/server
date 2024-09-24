package com.snackgame.server.game.snackgame.infinite.domain

import com.snackgame.server.game.session.exception.NoSuchSessionException
import com.snackgame.server.game.snackgame.core.domain.Percentile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
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

    @Modifying
    @Query("update SnackgameInfinite set ownerId = :toMemberId where ownerId = :fromMemberId")
    fun transferSessions(fromMemberId: Long, toMemberId: Long): Int

    fun deleteAllByOwnerId(memberId: Long)
}

fun SnackgameInifiniteRepository.getBy(ownerId: Long, sessionId: Long): SnackgameInfinite =
    findByOwnerIdAndSessionId(ownerId, sessionId) ?: throw NoSuchSessionException()

fun SnackgameInifiniteRepository.ratePercentileOf(sessionId: Long): Percentile {
    with(findPercentileOf(sessionId)) {
        this ?: throw NoSuchSessionException()
        return Percentile(this)
    }
}