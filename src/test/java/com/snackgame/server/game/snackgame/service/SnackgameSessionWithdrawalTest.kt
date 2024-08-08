package com.snackgame.server.game.snackgame.service

import com.snackgame.server.game.snackgame.domain.SnackgameRepository
import com.snackgame.server.history.fixture.SnackgameFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Suppress("NonAsciiCharacters")
@ServiceTest
class SnackgameSessionWithdrawalTest {

    @Autowired
    private lateinit var snackgameRepository: SnackgameRepository

    @Autowired
    private lateinit var snackgameSessionWithdrawal: SnackgameSessionWithdrawal

    @BeforeEach
    fun setUp() {
        SnackgameFixture.saveAll()
    }

    @Transactional
    @Test
    fun `회원의 세션을 모두 제거한다`() {
        snackgameSessionWithdrawal.executeOn(땡칠().id)

        assertThat(snackgameRepository.findAll()).noneMatch { it.ownerId == 땡칠().id }
    }
}
