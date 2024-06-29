package com.snackgame.server.rank.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.snackgame.server.applegame.exception.NoRankingYetException;

public interface BestScores extends JpaRepository<BestScore, Long> {

    @Query(
            value = "WITH best AS (select score, owner_id, season_id "
                    + "             from best_score best"
                    + "             order by score desc "
                    + "             limit :size) "
                    + "select rank() over (order by best.score desc) as `rank`, best.score, best.season_id, "
                    + "       m.id as owner_id, m.name as owner_name, mg.id as owner_group_id, mg.name as owner_group_name,"
                    + "       m.level as owner_level, m.profile_image as owner_profile_image "
                    + "from best "
                    + "         inner join member m on m.id = best.owner_id "
                    + "         left join member_group mg on mg.id = m.group_id",
            nativeQuery = true
    )
    List<BestScoreWithRankAndOwner> rankLeaders(int size);

    @Query(
            value = "WITH best AS (select score, owner_id, season_id "
                    + "             from best_score best"
                    + "             where season_id = :seasonId"
                    + "             order by score desc "
                    + "             limit :size) "
                    + "select rank() over (order by best.score desc) as `rank`, best.score, best.season_id,  "
                    + "       m.id as owner_id, m.name as owner_name, mg.id as owner_group_id, mg.name as owner_group_name,"
                    + "       m.level as owner_level, m.profile_image as owner_profile_image "
                    + "from best "
                    + "         inner join member m on m.id = best.owner_id "
                    + "         left join member_group mg on mg.id = m.group_id",
            nativeQuery = true
    )
    List<BestScoreWithRankAndOwner> rankLeadersBy(Long seasonId, int size);

    default BestScoreWithRankAndOwner rank(Long ownerId) {
        return findRankOf(ownerId)
                .orElseThrow(NoRankingYetException::new);
    }

    default BestScoreWithRankAndOwner rank(Long ownerId, Long seasonId) {
        return findRankOf(ownerId, seasonId)
                .orElseThrow(NoRankingYetException::new);
    }

    @Query(
            value = "WITH best AS (select rank() over (order by score desc) as `rank`, score, owner_id, season_id "
                    + "                                 from best_score best "
                    + "                                 where score >= ("
                    + "                                     select score from best_score where owner_id = :ownerId order by score desc limit 1"
                    + "                                 )) "
                    + "                    select best.rank, best.score, best.season_id, "
                    + "                           m.id as owner_id, m.name as owner_name, mg.id as owner_group_id, mg.name as owner_group_name,"
                    + "                           m.level as owner_level, m.profile_image as owner_profile_image "
                    + "                    from best "
                    + "                             inner join member m on m.id = best.owner_id "
                    + "                             left join member_group mg on mg.id = m.group_id "
                    + "                    where owner_id = :ownerId",
            nativeQuery = true
    )
    Optional<BestScoreWithRankAndOwner> findRankOf(Long ownerId);

    @Query(value = "WITH best AS (select rank() over (order by score desc) as `rank`, score, owner_id, season_id "
                   + "                                 from best_score best "
                   + "                                 where season_id = :seasonId and score >= ("
                   + "                                      select score from best_score where owner_id = :ownerId and season_id = :seasonId"
                   + "                                  )) "
                   + "                    select best.rank, best.score, best.season_id, "
                   + "                           m.id as owner_id, m.name as owner_name, mg.id as owner_group_id, mg.name as owner_group_name,"
                   + "                           m.level as owner_level, m.profile_image as owner_profile_image "
                   + "                    from best "
                   + "                             inner join member m on m.id = best.owner_id "
                   + "                             left join member_group mg on mg.id = m.group_id "
                   + "                    where owner_id = :ownerId",
            nativeQuery = true
    )
    Optional<BestScoreWithRankAndOwner> findRankOf(Long ownerId, Long seasonId);

    List<BestScore> findAllByOwnerId(Long ownerId);

    Optional<BestScore> findByOwnerIdAndSeasonId(Long ownerId, Long seasonId);

    default BestScore getByOwnerIdAndSeasonId(Long ownerId, Long seasonId) {
        return findByOwnerIdAndSeasonId(ownerId, seasonId)
                .orElse(BestScore.EMPTY);
    }
}
