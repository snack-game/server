package com.snackgame.server.game.snackgame.infinite.service


import com.snackgame.server.game.session.event.SessionEndEvent
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameEndResponse
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameResponse
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameUpdateRequest
import com.snackgame.server.game.snackgame.infinite.domain.SnackgameInfinite
import com.snackgame.server.game.snackgame.infinite.domain.SnackgameInifiniteRepository
import com.snackgame.server.game.snackgame.infinite.domain.getBy
import com.snackgame.server.game.snackgame.infinite.domain.ratePercentileOf
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SnackgameInfiniteService(
    private val snackGameRepository: SnackgameInifiniteRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun startSessionFor(memberId: Long): SnackgameResponse {
        val game = snackGameRepository.save(SnackgameInfinite(memberId))

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
