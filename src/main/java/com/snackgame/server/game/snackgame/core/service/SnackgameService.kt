package com.snackgame.server.game.snackgame.core.service


import com.snackgame.server.game.session.event.SessionEndEvent
import com.snackgame.server.game.session.exception.NoSuchSessionException
import com.snackgame.server.game.snackgame.core.domain.Percentile
import com.snackgame.server.game.snackgame.core.domain.Snackgame
import com.snackgame.server.game.snackgame.core.domain.SnackgameRepository
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameEndResponse
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameResponse
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameUpdateRequest
import com.snackgame.server.game.snackgame.core.service.dto.StreakRequest
import com.snackgame.server.game.snackgame.core.service.dto.StreakRequest.Companion.toStreak
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SnackgameService(
    private val snackGameRepository: SnackgameRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun startSessionFor(memberId: Long): SnackgameResponse {
        val game = snackGameRepository.save(Snackgame.ofRandomized(memberId))

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun update(memberId: Long, sessionId: Long, request: SnackgameUpdateRequest): SnackgameResponse {
        val game = snackGameRepository.getBy(memberId, sessionId)

        game.setScoreUnsafely(request.score)

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun placeMoves(memberId: Long, sessionId: Long, streakRequests: List<StreakRequest>): Optional<Snackgame> {
        val game = snackGameRepository.getBy(memberId, sessionId)
        val previous = game.board

        game.removeSnacks(toStreak(streakRequests))

        return if (!game.board.equals(previous)) {
            Optional.of<Snackgame>(game)
        } else Optional.empty<Snackgame>()

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
