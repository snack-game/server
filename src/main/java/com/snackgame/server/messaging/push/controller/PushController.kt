package com.snackgame.server.messaging.push.controller

import com.snackgame.server.auth.token.support.Authenticated
import com.snackgame.server.member.domain.Member
import com.snackgame.server.messaging.push.service.DeviceService
import com.snackgame.server.messaging.push.service.dto.DeviceTokenRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "🔔 알림")
@RestController
class PushController(private val deviceService: DeviceService) {

    @Operation(summary = "기기 등록", description = "기기를 등록한다")
    @PostMapping("/notifications/devices")
    fun registerDevice(
        @Authenticated member: Member,
        @RequestBody deviceTokenRequest: DeviceTokenRequest
    ) {
        deviceService.registerDeviceFor(member.id, deviceTokenRequest)
    }

}
