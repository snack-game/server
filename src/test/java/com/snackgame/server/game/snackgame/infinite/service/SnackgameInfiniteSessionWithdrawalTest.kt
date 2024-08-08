package com.snackgame.server.game.snackgame.infinite.service

import com.snackgame.server.game.snackgame.domain.SnackgameInfinite
import com.snackgame.server.game.snackgame.domain.SnackgameInifiniteRepository
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
class SnackgameInfiniteSessionWithdrawalTest {

    @Autowired
    private lateinit var snackgameInfiniteSessionWithdrawal: SnackgameInfiniteSessionWithdrawal

    @Autowired
    private lateinit var snackgameInfiniteRepository: SnackgameInifiniteRepository

    @BeforeEach
    fun setUp() {
        FixtureSaver.save(SnackgameInfinite(땡칠().id))
    }

    @Transactional
    @Test
    fun `회원의 세션을 모두 제거한다`() {
        snackgameInfiniteSessionWithdrawal.executeOn(땡칠().id)

        assertThat(snackgameInfiniteRepository.findAll()).noneMatch { it.ownerId == 땡칠().id }
    }
}
