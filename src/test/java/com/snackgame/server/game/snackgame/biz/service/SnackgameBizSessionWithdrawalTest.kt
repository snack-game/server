package com.snackgame.server.game.snackgame.biz.service

import com.snackgame.server.game.snackgame.biz.domain.SnackgameBizRepository
import com.snackgame.server.game.snackgame.infinite.domain.SnackgameInfinite
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.support.fixture.FixtureSaver
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Suppress("NonAsciiCharacters")
@ServiceTest
class SnackgameBizSessionWithdrawalTest {

    @Autowired
    private lateinit var snackgameBizSessionWithdrawal: SnackgameBizSessionWithdrawal

    @Autowired
    private lateinit var snackgameBizRepository: SnackgameBizRepository

    @BeforeEach
    fun setUp() {
        FixtureSaver.save(SnackgameInfinite(땡칠().id))
    }

    @Transactional
    @Test
    fun `회원의 세션을 모두 제거한다`() {
        snackgameBizSessionWithdrawal.executeOn(땡칠().id)

        assertThat(snackgameBizRepository.findAll()).noneMatch { it.ownerId == 땡칠().id }
    }
}
