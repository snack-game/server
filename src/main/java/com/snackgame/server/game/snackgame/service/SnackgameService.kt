package com.snackgame.server.game.snackgame.service

import com.snackgame.server.applegame.domain.game.Percentile
import com.snackgame.server.game.session.event.SessionEndEvent
import com.snackgame.server.game.session.exception.NoSuchSessionException
import com.snackgame.server.game.snackgame.domain.Snackgame
import com.snackgame.server.game.snackgame.domain.SnackgameRepository
import com.snackgame.server.game.snackgame.service.dto.SnackgameEndResponse
import com.snackgame.server.game.snackgame.service.dto.SnackgameResponse
import com.snackgame.server.game.snackgame.service.dto.SnackgameUpdateRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SnackgameService(
    private val snackGameRepository: SnackgameRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun startSessionFor(memberId: Long): SnackgameResponse {
        val game = snackGameRepository.save(Snackgame(memberId))

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun update(memberId: Long, sessionId: Long, request: SnackgameUpdateRequest): SnackgameResponse {
        val game = snackGameRepository.getBy(memberId, sessionId)

        game.setScoreUnsafely(request.score)

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun pause(memberId: Long, sessionId: Long): SnackgameResponse {
        val game = snackGameRepository.getBy(memberId, sessionId)

        game.pause()

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun resume(memberId: Long, sessionId: Long): SnackgameResponse {
        val game = snackGameRepository.getBy(memberId, sessionId)

        game.resume()

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun end(memberId: Long, sessionId: Long): SnackgameEndResponse {
        val game = snackGameRepository.getBy(memberId, sessionId)

        game.end()
        eventPublisher.publishEvent(SessionEndEvent.of(game))

        return SnackgameEndResponse.of(game, snackGameRepository.ratePercentileOf(sessionId))
    }
}

private fun SnackgameRepository.getBy(ownerId: Long, sessionId: Long): Snackgame =
    findByOwnerIdAndSessionId(ownerId, sessionId) ?: throw NoSuchSessionException()

private fun SnackgameRepository.ratePercentileOf(sessionId: Long): Percentile {
    with(findPercentileOf(sessionId)) {
        this ?: throw NoSuchSessionException()
        return Percentile(this)
    }
}
