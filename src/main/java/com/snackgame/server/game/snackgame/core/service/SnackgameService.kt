package com.snackgame.server.game.snackgame.core.service


import com.snackgame.server.game.session.event.SessionEndEvent
import com.snackgame.server.game.snackgame.core.domain.Snackgame
import com.snackgame.server.game.snackgame.core.domain.SnackgameRepository
import com.snackgame.server.game.snackgame.core.domain.Streak
import com.snackgame.server.game.snackgame.core.domain.getBy
import com.snackgame.server.game.snackgame.core.domain.item.ItemService
import com.snackgame.server.game.snackgame.core.domain.item.ItemType
import com.snackgame.server.game.snackgame.core.domain.ratePercentileOf
import com.snackgame.server.game.snackgame.core.service.dto.CoordinateRequest
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameEndResponse
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameResponse
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameUpdateRequest
import com.snackgame.server.game.snackgame.core.service.dto.StreaksRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SnackgameService(
    private val snackGameRepository: SnackgameRepository,
    private val itemService: ItemService,
    private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun startSessionFor(memberId: Long): SnackgameResponse {
        val game = snackGameRepository.save(Snackgame(memberId))

        return SnackgameResponse.of(game)
    }

    @Deprecated("대체", ReplaceWith("removeStreaks()"))
    @Transactional
    fun update(memberId: Long, sessionId: Long, request: SnackgameUpdateRequest): SnackgameResponse {
        val game = snackGameRepository.getBy(memberId, sessionId)

        game.setScoreUnsafely(request.score)

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun removeStreaks(memberId: Long, sessionId: Long, streaksRequest: StreaksRequest): SnackgameResponse {
        val game = snackGameRepository.getBy(memberId, sessionId)

        streaksRequest.toStreaks()
            .forEach { game.remove(it) }

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun useBomb(ownerId: Long, sessionId: Long, coordinateRequest: CoordinateRequest): SnackgameResponse {
        itemService.useItem(ownerId, ItemType.BOMB)

        val game = snackGameRepository.getBy(ownerId, sessionId)
        val bombCoordinate = coordinateRequest.toCoordinate().toBombCoordinate()
        game.bomb(Streak.bomb(bombCoordinate))

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun useFeverTime(ownerId: Long, sessionId: Long): SnackgameResponse {
        itemService.useItem(ownerId, ItemType.FEVER_TIME)

        val game = snackGameRepository.getBy(ownerId, sessionId)
        game.startFeverTime()

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
