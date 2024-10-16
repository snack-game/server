package com.snackgame.server.messaging.notification.controller

import com.snackgame.server.auth.token.support.Authenticated
import com.snackgame.server.member.domain.Member
import com.snackgame.server.messaging.notification.service.NotificationService
import com.snackgame.server.messaging.notification.service.dto.DeviceTokenRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "🔔 알림")
@RestController
class NotificationController(private val notificationService: NotificationService) {

    @Operation(summary = "일반 사용자 생성", description = "기기를 등록한다")
    @PostMapping("/notification/devices")
    fun registerDevice(
        @Authenticated member: Member,
        @RequestBody deviceTokenRequest: DeviceTokenRequest
    ) {
        notificationService.registerDeviceFor(member.id, deviceTokenRequest)
    }
}
