package com.snackgame.server.game.snackgame.biz.service


import com.snackgame.server.game.session.event.SessionEndEvent
import com.snackgame.server.game.snackgame.biz.domain.SnackgameBizV2
import com.snackgame.server.game.snackgame.biz.domain.SnackgameBizV2Repository
import com.snackgame.server.game.snackgame.biz.domain.getBy
import com.snackgame.server.game.snackgame.biz.domain.ratePercentileOf
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameEndResponse
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameResponse
import com.snackgame.server.game.snackgame.core.service.dto.StreaksRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SnackgameBizV2Service(
    private val snackgameBizRepository: SnackgameBizV2Repository,
    private val eventPublisher: ApplicationEventPublisher,
) {

    @Transactional
    fun startSessionFor(memberId: Long): SnackgameResponse {
        val game = snackgameBizRepository.save(SnackgameBizV2(memberId))

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun removeStreaks(memberId: Long, sessionId: Long, streaksRequest: StreaksRequest): SnackgameResponse {
        val game = snackgameBizRepository.getBy(memberId, sessionId)

        streaksRequest.toStreaks()
            .forEach { game.remove(it) }

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun pause(memberId: Long, sessionId: Long): SnackgameResponse {
        val game = snackgameBizRepository.getBy(memberId, sessionId)

        game.pause()

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun resume(memberId: Long, sessionId: Long): SnackgameResponse {
        val game = snackgameBizRepository.getBy(memberId, sessionId)

        game.resume()

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun end(memberId: Long, sessionId: Long): SnackgameEndResponse {
        val game = snackgameBizRepository.getBy(memberId, sessionId)

        game.end()
        // TODO: 외부 사용자 식별 작업 후 활성화
//        eventPublisher.publishEvent(SessionEndEvent.of(game))

        return SnackgameEndResponse.of(game, snackgameBizRepository.ratePercentileOf(sessionId))
    }
}
