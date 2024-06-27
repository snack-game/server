package com.snackgame.server.game.snackgame.domain

import com.snackgame.server.game.session.domain.Session
import java.time.Duration
import javax.persistence.Entity

@Entity
class SnackGame(ownerId: Long) : Session(ownerId, Duration.ofMinutes(2))
