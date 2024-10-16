@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.messaging.notification.service

import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.messaging.notification.service.dto.DeviceTokenRequest
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@ServiceTest
class NotificationServiceTest {

    @Autowired
    private lateinit var notificationService: NotificationService

    @Test
    fun `기기를 등록한다`() {
        val deviceToken = "a_device_token"
        notificationService.registerDeviceFor(땡칠().id, DeviceTokenRequest(deviceToken))

        assertThat(notificationService.getDevicesOf(땡칠().id))
            .singleElement()
            .matches { it.ownerId == 땡칠().id }
            .matches { it.token == deviceToken }
    }
}
