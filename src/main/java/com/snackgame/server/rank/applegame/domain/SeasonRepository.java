package com.snackgame.server.rank.applegame.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SeasonRepository extends JpaRepository<Season, Long> {

    @Query("SELECT count(id) <= 0 FROM Season")
    boolean isEmpty();
}
