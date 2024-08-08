@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.status

import com.snackgame.server.game.session.event.SessionEndEvent
import com.snackgame.server.history.fixture.SnackgameFixture
import com.snackgame.server.member.fixture.MemberFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.member.service.MemberAccountService
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@ServiceTest
class StatusServiceTest {

    @Autowired
    private lateinit var statusService: StatusService

    @Autowired
    private lateinit var memberAccountService: MemberAccountService

    @BeforeEach
    fun setUp() {
        MemberFixture.saveAll()
    }

    @Test
    fun `게임이 끝날때 게임 점수만큼 경험치를 부여한다`() {
        val session = SnackgameFixture.first()
        statusService.onSessionEnd(SessionEndEvent.of(session))

        val member = memberAccountService.getBy(땡칠().id)
        assertThat(member.status.exp).isEqualTo(session.score.toDouble())
    }
}
