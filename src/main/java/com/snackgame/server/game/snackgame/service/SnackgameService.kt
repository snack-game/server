package com.snackgame.server.game.snackgame.service

import com.snackgame.server.applegame.domain.game.Percentile
import com.snackgame.server.game.session.event.SessionEndEvent
import com.snackgame.server.game.session.exception.NoSuchSessionException
import com.snackgame.server.game.snackgame.domain.Snackgame
import com.snackgame.server.game.snackgame.domain.SnackgameRepository
import com.snackgame.server.game.snackgame.service.dto.SnackgameEndResponse
import com.snackgame.server.game.snackgame.service.dto.SnackgameResponse
import com.snackgame.server.game.snackgame.service.dto.SnackgameUpdateRequest
import com.snackgame.server.member.domain.MemberRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SnackgameService(
    private val snackGameRepository: SnackgameRepository,
    private val memberRepository: MemberRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun startSessionFor(memberId: Long): SnackgameResponse {
        val game = snackGameRepository.save(Snackgame(memberId))

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun update(memberId: Long, sessionId: Long, request: SnackgameUpdateRequest): SnackgameResponse {
        val game = snackGameRepository.getBy(sessionId)

        game.setScoreUnsafely(request.score)

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun pause(memberId: Long, sessionId: Long): SnackgameResponse {
        val game = snackGameRepository.getBy(sessionId)

        game.pause()

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun resume(memberId: Long, sessionId: Long): SnackgameResponse {
        val game = snackGameRepository.getBy(sessionId)

        game.resume()

        return SnackgameResponse.of(game)
    }

    @Transactional
    fun end(memberId: Long, sessionId: Long): SnackgameEndResponse {
        val game = snackGameRepository.getBy(sessionId)

        game.end()

        eventPublisher.publishEvent(SessionEndEvent.of(game))
        // TODO: 모듈화 + 이벤트 기반으로 동작하도록 분리. 게임은 게임에만 집중하도록.
        val member = memberRepository.getById(memberId)
        member.status.addExp(game.score.toDouble())

        return SnackgameEndResponse.of(game, snackGameRepository.ratePercentileOf(sessionId))
    }
}

private fun SnackgameRepository.getBy(sessionId: Long): Snackgame =
    findByIdOrNull(sessionId) ?: throw NoSuchSessionException()

private fun SnackgameRepository.ratePercentileOf(sessionId: Long): Percentile {
    with(findPercentileOf(sessionId)) {
        this ?: throw NoSuchSessionException()
        return Percentile(this)
    }
}
