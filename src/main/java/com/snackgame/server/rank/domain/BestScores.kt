package com.snackgame.server.rank.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import javax.persistence.LockModeType

interface BestScores : JpaRepository<BestScore, Long> {
    @Query(
        value = """
            with best AS (select owner_id, game_id, season_id, score
                from best_score best
                where (:seasonId is null or season_id = :seasonId) and game_id = :gameId and is_ranked = true
                order by score desc
                limit :size
            )
            select rank() over (order by best.score desc) as `rank`, best.owner_id, best.game_id, best.season_id, best.score,
                m.name as owner_name, mg.id as owner_group_id, mg.name as owner_group_name,
                m.level as owner_level, m.profile_image as owner_profile_image 
            from best inner join member m on m.id = best.owner_id
            left join member_group mg on mg.id = m.group_id
        """,
        nativeQuery = true
    )
    fun rankLeadersBy(size: Int, gameId: Long, seasonId: Long? = null): List<BestScoreWithRankAndOwner>

    @Query(
        value = """
            with best AS (
                select rank() over (order by score desc) as `rank`, owner_id, game_id, season_id, score
                from best_score best
                where (:seasonId is null or season_id = :seasonId) and game_id = :gameId and is_ranked = true
                    and score >= (
                        select score from best_score 
                        where owner_id = :ownerId and (:seasonId is null or season_id = :seasonId) and game_id = :gameId 
                        order by score desc limit 1
                    )
            )
            select best.rank, best.owner_id, best.game_id, best.season_id, best.score,
                m.name as owner_name, mg.id as owner_group_id, mg.name as owner_group_name,
                m.level as owner_level, m.profile_image as owner_profile_image
            from best inner join member m on m.id = best.owner_id 
            left join member_group mg on mg.id = m.group_id where owner_id = :ownerId""",
        nativeQuery = true
    )
    fun findRankOf(ownerId: Long, gameId: Long, seasonId: Long? = null): BestScoreWithRankAndOwner?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByOwnerIdAndGameIdAndSeasonId(ownerId: Long, gameId: Long, seasonId: Long): BestScore?

    fun deleteAllByOwnerId(ownerId: Long)
}
