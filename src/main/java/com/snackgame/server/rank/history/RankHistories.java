package com.snackgame.server.rank.history;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RankHistories extends JpaRepository<RankHistory, Long> {

    RankHistory findByOwnerId(Long ownerId);

    @Modifying
    @Query(value = "update rank_history "
                   + "set current_rank = current_rank + 1 "
                   + "where current_rank >= :newRank and owner_id != :ownerId"
            , nativeQuery = true)
    void update(Long ownerId, Long newRank);

    @Query(value = "SELECT rh.id AS id, rh.owner_id AS ownerId, rh.before_rank AS beforeRank, " +
                   "rh.current_rank AS currentRank, m.name AS name " +
                   "FROM rank_history rh " +
                   "JOIN member m ON rh.owner_id = m.id " +
                   "WHERE rh.current_rank > (SELECT current_rank FROM rank_history WHERE owner_id = :ownerId) " +
                   "ORDER BY rh.current_rank ASC " +
                   "LIMIT LEAST(:size, (SELECT (before_rank - current_rank) FROM rank_history "
                   + "WHERE owner_id = :ownerId)) ",
            nativeQuery = true)
    List<RankHistoryWithName> findBelowWithName(@Param("ownerId") Long ownerId, @Param("size") int size);


}
