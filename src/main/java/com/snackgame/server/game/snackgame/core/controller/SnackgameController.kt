package com.snackgame.server.game.snackgame.core.controller

import com.snackgame.server.auth.token.support.Authenticated
import com.snackgame.server.game.snackgame.core.domain.item.ItemService
import com.snackgame.server.game.snackgame.core.service.SnackgameService
import com.snackgame.server.game.snackgame.core.service.dto.CoordinateRequest
import com.snackgame.server.game.snackgame.core.service.dto.ItemGrantRequest
import com.snackgame.server.game.snackgame.core.service.dto.ItemResponse
import com.snackgame.server.game.snackgame.core.service.dto.ItemsResponse
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameEndResponse
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameResponse
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameUpdateRequest
import com.snackgame.server.game.snackgame.core.service.dto.StreaksRequest
import com.snackgame.server.member.domain.Member
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
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
class

SnackgameController(
    private val snackgameService: SnackgameService,
    private val itemService: ItemService
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
        summary = "스트릭 추가",
        description = """
스트릭을 순서대로 전달하여 게임을 검증한다.  
황금 스낵을 제거한 경우 세션 정보가 함께 응답된다.
"""
    )
    @PostMapping("/{sessionId}/streaks")
    fun removeStreaks(
        @Authenticated member: Member,
        @PathVariable sessionId: Long,
        @RequestBody streaksRequest: StreaksRequest
    ): ResponseEntity<SnackgameResponse> {
        val game = snackgameService.removeStreaks(member.id, sessionId, streaksRequest)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(game)
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

    @Operation(summary = "사용자가 가진 아이템 조회", description = "사용자가 아이템을 각각 몇 개 소유하고 있는지 조회한다")
    @GetMapping("/items")
    fun checkItemPresence(
        @Authenticated member: Member,
    ): ItemsResponse {
        return itemService.checkItemPresence(member.id)
    }

    @Operation(summary = "폭탄 아이템 사용", description = "폭탄 아이템으로 선택한 좌표를 입력하면 사용할 수 있다")
    @PostMapping("/{sessionId}/bomb")
    fun useBomb(
        @Authenticated member: Member,
        @PathVariable sessionId: Long,
        @RequestBody coordinateRequest: CoordinateRequest
    ): ResponseEntity<SnackgameResponse> {
        val game = snackgameService.useBomb(member.id, sessionId, coordinateRequest)
        return ResponseEntity.ok(game)
    }

    @Operation(summary = "피버타임 아이템 사용", description = "30초 동안 점수를 두배 증가시킨다")
    @PostMapping("/{sessionId}/fever-time")
    fun useFeverTime(
        @Authenticated member: Member,
        @PathVariable sessionId: Long
    ): ResponseEntity<SnackgameResponse> {
        val game = snackgameService.useFeverTime(member.id, sessionId)
        return ResponseEntity.ok(game)
    }

    @Operation(summary = "아이템 획득", description = "사용자가 특정 아이템을 얻을 수 있다")
    @PostMapping("/item")
    fun provideItem(
        @Authenticated member: Member,
        @RequestBody itemGrantRequest: ItemGrantRequest,
    ): ItemResponse {
        return itemService.issueItem(member.id, itemGrantRequest.itemType, itemGrantRequest.grantType)
    }

}
