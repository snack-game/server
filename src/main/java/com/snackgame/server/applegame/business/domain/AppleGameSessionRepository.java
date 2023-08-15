package com.snackgame.server.applegame.business.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppleGameSessionRepository extends JpaRepository<AppleGame, Long> {
}
