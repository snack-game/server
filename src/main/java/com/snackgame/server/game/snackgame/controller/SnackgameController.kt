package com.snackgame.server.game.snackgame.controller

import com.snackgame.server.auth.token.support.Authenticated
import com.snackgame.server.game.snackgame.service.SnackgameService
import com.snackgame.server.game.snackgame.service.dto.SnackgameEndResponse
import com.snackgame.server.game.snackgame.service.dto.SnackgameResponse
import com.snackgame.server.game.snackgame.service.dto.SnackgameUpdateRequest
import com.snackgame.server.game.snackgame.service.dto.StreakRequest
import com.snackgame.server.member.domain.Member
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Tag(name = "🍿 스낵게임")
@RequestMapping("/games/2")
@RestController
class SnackgameController(
    private val snackgameService: SnackgameService
) {

    @Operation(
        summary = "스낵게임 세션 시작",
        description = """
스낵게임 세션을 시작한다. 

게임 보드는 추후 제공 예정"""
    )
    @PostMapping
    fun startSessionFor(@Authenticated member: Member): SnackgameResponse =
        snackgameService.startSessionFor(member.id)

    @Operation(
        summary = "[임시] 스낵게임 세션 수정",
        description = """
세션을 수정한다.

현재는 점수 수정만 가능하며, 기존 점수가 덮어쓰기된다."""
    )
    @PutMapping("/{sessionId}")
    fun update(
        @Authenticated member: Member,
        @PathVariable sessionId: Long,
        @RequestBody @Valid request: SnackgameUpdateRequest,
    ): SnackgameResponse = snackgameService.update(member.id, sessionId, request)

    @Operation(
        summary = "스낵게임 세션 수 삽입",
        description = """
            지정한 세션에 수들을 삽입한다. 황금사과를 제거한 경우 초기화된 판을 응답한다.
        """

    )
    @PutMapping("/{sessionId}/moves")
    fun placeMoves(
        @Authenticated member: Member,
        @PathVariable sessionId: Long,
        @RequestBody @Valid requests: List<StreakRequest>
    ): ResponseEntity<SnackgameResponse> = snackgameService.placeMoves(member.id, sessionId, requests)
        .map { game ->
            ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SnackgameResponse.of(game))
        }
        .orElseGet {
            ResponseEntity.ok().build()
        }

    @Operation(
        summary = "스낵게임 세션 일시정지",
        description = """
해당 세션이 일시정지된다.

일시정지된 세션은 별도로 종료하지 않아도 **7일** 후 자동으로 만료된다."""
    )
    @PostMapping("/{sessionId}/pause")
    fun pause(@Authenticated member: Member, @PathVariable sessionId: Long): SnackgameResponse =
        snackgameService.pause(member.id, sessionId)

    @Operation(summary = "스낵게임 세션 재개", description = "해당 세션을 재개한다")
    @PostMapping("/{sessionId}/resume")
    fun resume(@Authenticated member: Member, @PathVariable sessionId: Long): SnackgameResponse =
        snackgameService.resume(member.id, sessionId)

    @Operation(summary = "스낵게임 세션 종료", description = "세션을 종료한다")
    @PostMapping("/{sessionId}/end")
    fun end(@Authenticated member: Member, @PathVariable sessionId: Long): SnackgameEndResponse =
        snackgameService.end(member.id, sessionId)
}
