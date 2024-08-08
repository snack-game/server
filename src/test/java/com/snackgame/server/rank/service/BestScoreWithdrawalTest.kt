package com.snackgame.server.rank.service

import com.snackgame.server.fixture.BestScoreFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.rank.domain.BestScores
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Suppress("NonAsciiCharacters")
@ServiceTest
class BestScoreWithdrawalTest {

    @Autowired
    private lateinit var bestScores: BestScores

    @Autowired
    private lateinit var bestScoreWithdrawal: BestScoreWithdrawal

    @BeforeEach
    fun setUp() {
        BestScoreFixture.saveAll()
    }

    @Transactional
    @Test
    fun `회원의 최고점수를 모두 제거한다`() {
        bestScoreWithdrawal.executeOn(땡칠().id)

        assertThat(bestScores.findAll()).noneMatch { it.ownerId == 땡칠().id }
    }
}
