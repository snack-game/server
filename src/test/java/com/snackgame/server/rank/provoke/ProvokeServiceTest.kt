package com.snackgame.server.rank.provoke

import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.member.fixture.MemberFixture.정환
import com.snackgame.server.messaging.push.fixture.DeviceFixture
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


@ServiceTest
class ProvokeServiceTest {

    @Autowired
    lateinit var provokeService: ProvokeService

    @Test
    fun `도발이 준비되었는지 확인할 수 있다`() {

        provokeService.reserveProvoking(땡칠().id, 1)

        assertThat(provokeService.checkStatus(땡칠().id, 1)).isTrue
    }

    @Test
    fun `도발하면 알림을 전송할 수 있다`() {

        DeviceFixture.saveAll()

        val future = provokeService.provoke(정환().id, 정환().getNameAsString())

        future.get()
    }

    @Test
    fun `도발 대상이 없다면 false를 반환한다`() {

        val status = provokeService.checkStatus(땡칠().id, 1)

        assertThat(status).isFalse
    }

}
