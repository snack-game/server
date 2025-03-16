package com.snackgame.server.messaging.push.service


import com.snackgame.server.member.fixture.MemberFixture
import com.snackgame.server.member.fixture.MemberFixture.정환
import com.snackgame.server.messaging.push.fixture.DeviceFixture
import com.snackgame.server.messaging.push.service.dto.NotificationRequest
import com.snackgame.server.support.general.ServiceTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@ServiceTest
@Disabled
class FCMPushServiceTest {


    @Autowired
    lateinit var fcmPushService: FCMPushService

    @BeforeEach
    fun setUp() {
        MemberFixture.saveAll()
        DeviceFixture.saveAll()
    }

    @DisplayName("토큰을 통해 푸시알림을 전송할 수 있다")
    @Test
    fun sendMessage() {

        val future = fcmPushService.sendPushMessage(
            NotificationRequest("테스트", "테스트"),
            정환().id
        )
        future.get()
    }

}
