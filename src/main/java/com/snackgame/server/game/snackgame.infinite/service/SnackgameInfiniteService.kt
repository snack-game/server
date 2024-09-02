package com.snackgame.server.game.snackgame.service


import com.snackgame.server.game.session.event.SessionEndEvent
import com.snackgame.server.game.session.exception.NoSuchSessionException
import com.snackgame.server.game.snackgame.domain.Percentile
import com.snackgame.server.game.snackgame.domain.SnackgameInfinite
import com.snackgame.server.game.snackgame.domain.SnackgameInifiniteRepository
import com.snackgame.server.game.snackgame.service.dto.SnackgameInfiniteEndResponse
import com.snackgame.server.game.snackgame.service.dto.SnackgameInfiniteResponse
import com.snackgame.server.game.snackgame.service.dto.SnackgameInfiniteUpdateRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SnackgameInfiniteService(
    private val snackGameRepository: SnackgameInifiniteRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun startSessionFor(memberId: Long): SnackgameInfiniteResponse {
        val game = snackGameRepository.save(SnackgameInfinite(memberId))

        return SnackgameInfiniteResponse.of(game)
    }

    @Transactional
    fun update(memberId: Long, sessionId: Long, request: SnackgameInfiniteUpdateRequest): SnackgameInfiniteResponse {
        val game = snackGameRepository.getBy(memberId, sessionId)

        game.setScoreUnsafely(request.score)

        return SnackgameInfiniteResponse.of(game)
    }

    @Transactional
    fun pause(memberId: Long, sessionId: Long): SnackgameInfiniteResponse {
        val game = snackGameRepository.getBy(memberId, sessionId)

        game.pause()

        return SnackgameInfiniteResponse.of(game)
    }

    @Transactional
    fun resume(memberId: Long, sessionId: Long): SnackgameInfiniteResponse {
        val game = snackGameRepository.getBy(memberId, sessionId)

        game.resume()

        return SnackgameInfiniteResponse.of(game)
    }

    @Transactional
    fun end(memberId: Long, sessionId: Long): SnackgameInfiniteEndResponse {
        val game = snackGameRepository.getBy(memberId, sessionId)

        game.end()
        eventPublisher.publishEvent(SessionEndEvent.of(game))

        return SnackgameInfiniteEndResponse.of(game, snackGameRepository.ratePercentileOf(sessionId))
    }
}

private fun SnackgameInifiniteRepository.getBy(ownerId: Long, sessionId: Long): SnackgameInfinite =
    findByOwnerIdAndSessionId(ownerId, sessionId) ?: throw NoSuchSessionException()

private fun SnackgameInifiniteRepository.ratePercentileOf(sessionId: Long): Percentile {
    with(findPercentileOf(sessionId)) {
        this ?: throw NoSuchSessionException()
        return Percentile(this)
    }
}
