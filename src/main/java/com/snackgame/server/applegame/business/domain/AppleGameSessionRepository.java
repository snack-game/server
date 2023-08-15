package com.snackgame.server.applegame.business.domain;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppleGameSessionRepository extends JpaRepository<AppleGame, Long> {

    List<AppleGame> findAllByIsEndedIsTrue(Pageable pageable);
}
