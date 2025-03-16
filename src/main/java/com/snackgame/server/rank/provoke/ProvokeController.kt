package com.snackgame.server.rank.provoke

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ProvokeController(private val provokeService: ProvokeService) {

    @GetMapping("provoke/{ownerId}/{sessionId}")
    fun checkStatus(
        @PathVariable ownerId: Long,
        @PathVariable sessionId: Long
    ): ResponseEntity<Boolean> {
        val isRankHistoryUpdated = provokeService.checkStatus(ownerId, sessionId)
        return ResponseEntity.status(HttpStatus.OK).body(isRankHistoryUpdated)
    }

    @PostMapping("provoke/send/{ownerId}")
    fun provoking(
        @PathVariable ownerId: Long,
        receiverNickname: String
    ) {
        provokeService.provoke(ownerId, receiverNickname)
    }
}
