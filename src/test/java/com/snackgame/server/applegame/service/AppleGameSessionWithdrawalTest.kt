package com.snackgame.server.applegame.service

import com.snackgame.server.applegame.domain.game.AppleGames
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Suppress("NonAsciiCharacters")
@ServiceTest
class AppleGameSessionWithdrawalTest {


    @Autowired
    private lateinit var appleGameService: AppleGameService

    @Autowired
    private lateinit var appleGames: AppleGames

    @Autowired
    private lateinit var appleGameSessionWithdrawal: AppleGameSessionWithdrawal

    @BeforeEach
    fun setUp() {
        appleGameService.startGameFor(땡칠().id)
    }

    @Transactional
    @Test
    fun `회원의 세션을 모두 제거한다`() {
        appleGameSessionWithdrawal.executeOn(땡칠().id)

        assertThat(appleGames.findAll()).noneMatch { it.ownerId == 땡칠().id }
    }
}
