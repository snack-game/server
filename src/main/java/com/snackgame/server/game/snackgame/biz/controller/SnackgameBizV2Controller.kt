package com.snackgame.server.game.snackgame.biz.controller

import com.snackgame.server.auth.token.util.JwtProvider
import com.snackgame.server.game.sign.service.Signed
import com.snackgame.server.game.snackgame.biz.service.SnackgameBizV2Service
import com.snackgame.server.game.snackgame.biz.service.dto.SnackgameBizStartResponse
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameEndResponse
import com.snackgame.server.game.snackgame.core.service.dto.SnackgameResponse
import com.snackgame.server.game.snackgame.core.service.dto.StreaksRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.InetAddress
import kotlin.random.Random

@Tag(name = "🍿 스낵게임 Biz V2")
@RequestMapping("/games/5")
@RestController
class SnackgameBizV2Controller(
    private val snackgameBizService: SnackgameBizV2Service,
    private val accessTokenProvider: JwtProvider
) {

    @Operation(
        summary = "스낵게임 세션 시작",
        description = """
스낵게임 세션을 시작한다."""
    )
    @PostMapping
    fun startSessionFor(
        @RequestHeader("X-Forwarded-For", required = false) originalIp: String?
    ): SnackgameBizStartResponse {
        // TODO: 사용자를 직접 식별하여 id를 만든다.
        val temporaryUserId = originalIp?.let { ipToLong(it) } ?: Random.nextLong()

        val game = snackgameBizService.startSessionFor(temporaryUserId)

        val token = accessTokenProvider.createTokenWith(temporaryUserId.toString())

        return SnackgameBizStartResponse.of(game, token)
    }

    // TODO: 사용자를 직접 식별하여 id를 만든다.
    fun ipToLong(ip: String): Long {
        val inetAddress = InetAddress.getByName(ip)
        val addressBytes = inetAddress.address
        var result: Long = 0
        for (byte in addressBytes) {
            result = (result shl 8) or (byte.toInt() and 0xFF).toLong()
        }
        return result
    }

    @Operation(
        summary = "스트릭 추가",
        description = """
스트릭을 순서대로 전달하여 게임을 검증한다.  
황금 스낵을 제거한 경우 세션 정보가 함께 응답된다.
"""
    )
    @PostMapping("/{sessionId}/streaks")
    fun removeStreaks(
        @RequestHeader("Authorization") authorization: String,
        @PathVariable sessionId: Long,
        @RequestBody streaksRequest: StreaksRequest
    ): ResponseEntity<SnackgameResponse> {
        val memberId = accessTokenProvider.getSubjectFrom(authorization).toLong()
        val game = snackgameBizService.removeStreaks(memberId, sessionId, streaksRequest)

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
    fun pause(
        @RequestHeader("Authorization") authorization: String,
        @PathVariable sessionId: Long
    ): SnackgameResponse {
        val memberId = accessTokenProvider.getSubjectFrom(authorization).toLong()
        return snackgameBizService.pause(memberId, sessionId)
    }

    @Operation(summary = "스낵게임 세션 재개", description = "해당 세션을 재개한다")
    @PostMapping("/{sessionId}/resume")
    fun resume(
        @RequestHeader("Authorization") authorization: String,
        @PathVariable sessionId: Long
    ): SnackgameResponse {
        val memberId = accessTokenProvider.getSubjectFrom(authorization).toLong()
        return snackgameBizService.resume(memberId, sessionId)
    }

    @Signed
    @Operation(summary = "스낵게임 세션 종료", description = "세션을 종료한다")
    @PostMapping("/{sessionId}/end")
    fun end(
        @RequestHeader("Authorization") authorization: String,
        @PathVariable sessionId: Long
    ): SnackgameEndResponse {
        val memberId = accessTokenProvider.getSubjectFrom(authorization).toLong()
        return snackgameBizService.end(memberId, sessionId)
    }
}
