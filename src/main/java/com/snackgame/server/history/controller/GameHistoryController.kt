package com.snackgame.server.history.controller

import com.snackgame.server.auth.token.support.Authenticated
import com.snackgame.server.history.controller.dto.GameHistoryResponse
import com.snackgame.server.history.dao.SnackgameHistoryDao
import com.snackgame.server.member.domain.Member
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "📈 전적")
@RestController
class GameHistoryController(
    private val snackgameHistoryDao: SnackgameHistoryDao
) {

    @GetMapping("/histories/me")
    @Operation(
        summary = "자신의 게임 전적 조회",
        description = """
        `DATE`: 최근 7일의 최고 점수 추이를 조회한다.

        `SESSION`: 최근 25게임의 점수들을 조회한다."""
    )
    fun showScoresBySessions(
        @Authenticated member: Member,
        @RequestParam("by") criteria: Criteria
    ): List<GameHistoryResponse> {
        if (criteria == Criteria.DATE) {
            return snackgameHistoryDao.selectByDate(member.id)
        }
        return snackgameHistoryDao.selectBySession(member.id, GAME_RECORD_SIZE)
    }

    enum class Criteria {
        DATE,
        SESSION
    }

    companion object {
        private const val GAME_RECORD_SIZE = 25
    }
}
