package com.snackgame.server.rank.applegame.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SeasonRepository extends JpaRepository<Season, Long> {

    @Query("SELECT count(id) <= 0 FROM Season")
    boolean isEmpty();

    Optional<Season> findFirstByOrderByStartedAtDesc();

    default Season getLatest() {
        return findFirstByOrderByStartedAtDesc()
                .orElseThrow(() -> new IllegalStateException("시즌이 없습니다"));
    }
}
