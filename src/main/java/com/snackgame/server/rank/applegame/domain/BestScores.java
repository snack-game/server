package com.snackgame.server.rank.applegame.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.snackgame.server.applegame.exception.NoRankingYetException;

public interface BestScores extends JpaRepository<BestScore, Long> {

    @Query(
            value = "WITH best AS (select score, owner_id "
                    + "             from best_score best"
                    + "             order by score desc "
                    + "             limit :size) "
                    + "select rank() over (order by best.score desc) as `rank`, best.score, "
                    + "       m.id as owner_id, m.name as owner_name, mg.id as owner_group_id, mg.name as owner_group_name "
                    + "from best "
                    + "         inner join member m on m.id = best.owner_id "
                    + "         left join member_group mg on mg.id = m.group_id",
            nativeQuery = true
    )
    List<BestScoreWithRankAndOwner> rank(int size);

    @Query(
            value = "WITH best AS (select rank() over (order by score desc) as `rank`, score, owner_id "
                    + "                                 from best_score best "
                    + "                                 where score >= (select score from best_score where owner_id = :ownerId) "
                    + "                                 order by score desc) "
                    + "                    select best.rank, best.score, "
                    + "                           m.id as owner_id, m.name as owner_name, mg.id as owner_group_id, mg.name as owner_group_name "
                    + "                    from best "
                    + "                             inner join member m on m.id = best.owner_id "
                    + "                             left join member_group mg on mg.id = m.group_id "
                    + "                    where owner_id = :ownerId",
            nativeQuery = true
    )
    Optional<BestScoreWithRankAndOwner> findRankOf(Long ownerId);

    default BestScoreWithRankAndOwner rank(Long ownerId) {
        return findRankOf(ownerId)
                .orElseThrow(NoRankingYetException::new);
    }

    Optional<BestScore> findByOwnerId(Long ownerId);

    default BestScore getByOwnerId(Long ownerId) {
        return findByOwnerId(ownerId)
                .orElseGet(() -> save(new BestScore(ownerId)));
    }
}
