package com.snackgame.server.rank.service

import com.snackgame.server.fixture.BestScoreFixture
import com.snackgame.server.fixture.BestScoreFixture.사과게임_베타시즌_땡칠_10점
import com.snackgame.server.fixture.BestScoreFixture.사과게임_시즌1_땡칠_20점
import com.snackgame.server.fixture.BestScoreFixture.스낵게임_시즌1_땡칠_20점
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.member.fixture.MemberFixture.정언
import com.snackgame.server.rank.domain.BestScore
import com.snackgame.server.rank.domain.BestScores
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Suppress("NonAsciiCharacters")
@ServiceTest
class BestScoreTransferTest {

    @Autowired
    private lateinit var bestScores: BestScores

    @Autowired
    private lateinit var bestScoreTransfer: BestScoreTransfer

    @Transactional
    @Test
    fun `한 사용자의 최고점수들을 다른 사용자에게 모두 이전한다`() {
        BestScoreFixture.saveAll()

        bestScoreTransfer.execute(땡칠().id, 정언().id)

        assertThatBestScoreEquals(정언().id, 사과게임_베타시즌_땡칠_10점())
        assertThatBestScoreEquals(정언().id, 사과게임_시즌1_땡칠_20점())
        assertThatBestScoreEquals(정언().id, 스낵게임_시즌1_땡칠_20점())
    }

    private fun assertThatBestScoreEquals(memberId: Long, bestScore: BestScore) {
        val rank = bestScores.findRankOf(memberId, bestScore.gameId, bestScore.seasonId)!!

        assertThat(rank.score).isEqualTo(bestScore.score)
    }
}
