@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.messaging.notification.service

import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.messaging.notification.exception.DuplicatedDeviceException
import com.snackgame.server.messaging.notification.service.dto.DeviceTokenRequest
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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

    @Test
    fun `한 기기는 한 계정당 한 번만 등록할 수 있다`() {
        val deviceToken = "a_device_token"
        notificationService.registerDeviceFor(땡칠().id, DeviceTokenRequest(deviceToken))

        assertThatThrownBy { notificationService.registerDeviceFor(땡칠().id, DeviceTokenRequest(deviceToken)) }
            .isInstanceOf(DuplicatedDeviceException::class.java)
    }
}
