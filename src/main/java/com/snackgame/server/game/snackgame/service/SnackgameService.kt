package com.snackgame.server.game.snackgame.service

import com.snackgame.server.game.session.exception.NoSuchSessionException
import com.snackgame.server.game.snackgame.domain.Snackgame
import com.snackgame.server.game.snackgame.domain.SnackgameRepository
import com.snackgame.server.game.snackgame.service.dto.SnackgameResponse
import com.snackgame.server.game.snackgame.service.dto.SnackgameUpdateRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SnackgameService(
    private val snackGameRepository: SnackgameRepository
) {

    @Transactional
    fun start(memberId: Long): SnackgameResponse {
        val game = snackGameRepository.save(Snackgame(memberId))

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun pause(memberId: Long, sessionId: Long): SnackgameResponse {
        val game = snackGameRepository.findByIdOrNull(sessionId) ?: throw NoSuchSessionException()

        game.pause()

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun end(memberId: Long, sessionId: Long): SnackgameResponse {
        val game = snackGameRepository.findByIdOrNull(sessionId) ?: throw NoSuchSessionException()

        game.end()

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun update(memberId: Long, sessionId: Long, request: SnackgameUpdateRequest): SnackgameResponse {
        val game = snackGameRepository.findByIdOrNull(sessionId) ?: throw NoSuchSessionException()

        game.score = request.score

        return SnackgameResponse.of(game)
    }
}
