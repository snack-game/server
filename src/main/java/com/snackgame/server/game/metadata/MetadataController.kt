package com.snackgame.server.game.metadata

import com.snackgame.server.game.session.domain.SessionStateType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "🎮 게임")
@RequestMapping("/games")
@RestController
class MetadataController {

    @Operation(summary = "게임 메타데이터 나열")
    @GetMapping
    fun enumerateMetadata(): List<MetadataResponse> {
        return Metadata.entries.map { MetadataResponse(it.gameId, it.localizedName) }
    }

    @Operation(summary = "세션 상태 나열")
    @GetMapping("/session-states")
    fun enumerateSessionStates(): List<SessionStateType> {
        return SessionStateType.entries
    }
}
