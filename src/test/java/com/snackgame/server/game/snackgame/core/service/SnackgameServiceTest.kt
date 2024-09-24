@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.game.snackgame.core.service

import com.snackgame.server.fixture.SeasonFixture
import com.snackgame.server.game.snackgame.core.domain.Snackgame
import com.snackgame.server.game.snackgame.core.domain.SnackgameRepository
import com.snackgame.server.game.snackgame.core.service.dto.CoordinateRequest
import com.snackgame.server.game.snackgame.core.service.dto.StreaksRequest
import com.snackgame.server.game.snackgame.fixture.TestFixture
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
        val game = snackgameRepository.save(Snackgame(땡칠().id, TestFixture.TWO_BY_FOUR()))

        val coordinates = listOf(
            CoordinateRequest(1, 0),
            CoordinateRequest(0, 0)
        )

        snackgameService.removeStreaks(땡칠().id, game.sessionId, StreaksRequest(listOf(coordinates)))

        val found = snackgameRepository.findByOwnerIdAndSessionId(땡칠().id, game.sessionId)!!
        assertThat(found.score).isEqualTo(2)
    }
}
