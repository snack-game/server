package com.snackgame.server.game.snackgame.domain;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SnackgameRepository : JpaRepository<Snackgame, Long> {
}
