package com.snackgame.server.rank.history;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RankHistories extends JpaRepository<RankHistory, Long> {

    RankHistory findByOwnerId(Long ownerId);

    @Modifying
    @Query(value = "update rank_history "
                   + "set before_rank = before_rank + 1 "
                   + "where before_rank >= :newRank and owner_id != :ownerId"
            , nativeQuery = true)
    void update(Long ownerId, Long newRank);

    @Query(value = "select * "
                   + "from rank_history "
                   + "where owner_id != :ownerId "
                   + "order by before_rank ASC "
                   + "limit :size"
            , nativeQuery = true)
    List<RankHistory> findBelow(Long ownerId, int size);

}
