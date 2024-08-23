package com.snackgame.server.game.snackgame.service

import com.snackgame.server.fixture.SeasonFixture
import com.snackgame.server.game.snackgame.domain.Snackgame
import com.snackgame.server.game.snackgame.domain.SnackgameRepository
import com.snackgame.server.game.snackgame.fixture.TestFixture
import com.snackgame.server.game.snackgame.service.dto.StreakRequest
import com.snackgame.server.member.fixture.MemberFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@ServiceTest
class SnackgameServiceTest {

    @Autowired
    private lateinit var snackgameService: SnackgameService

    @Autowired
    private lateinit var snackgameRepository: SnackgameRepository

    @BeforeEach
    fun setUp() {
        MemberFixture.saveAll()
        SeasonFixture.saveAll()
    }

    @Test
    fun `게임을 조작한다`() {
        var game = snackgameRepository.save(Snackgame(땡칠().id, TestFixture.TWO_BY_FOUR()))

        var streakRequests = listOf<StreakRequest>(
            StreakRequest(1, 0),
            StreakRequest(0, 0)
        )

        snackgameService.placeMoves(땡칠().id, game.sessionId, streakRequests)

        var found = snackgameRepository.findByOwnerIdAndSessionId(땡칠().id, game.sessionId)
        assertThat(found?.score).isEqualTo(2)
    }
}