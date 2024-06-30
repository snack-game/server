@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.rank.domain

import com.snackgame.server.fixture.BestScoreFixture
import com.snackgame.server.fixture.BestScoreFixture.사과게임_베타시즌_땡칠_10점
import com.snackgame.server.fixture.BestScoreFixture.사과게임_베타시즌_똥수_10점
import com.snackgame.server.fixture.BestScoreFixture.사과게임_베타시즌_유진_6점
import com.snackgame.server.fixture.BestScoreFixture.사과게임_베타시즌_정언_8점
import com.snackgame.server.fixture.BestScoreFixture.사과게임_베타시즌_정환_8점
import com.snackgame.server.fixture.BestScoreFixture.사과게임_시즌1_땡칠_20점
import com.snackgame.server.fixture.BestScoreFixture.사과게임_시즌1_유진_20점
import com.snackgame.server.fixture.BestScoreFixture.사과게임_시즌1_정언_8점
import com.snackgame.server.fixture.BestScoreFixture.사과게임_시즌1_정환_20점
import com.snackgame.server.game.metadata.Metadata
import com.snackgame.server.member.fixture.MemberFixture
import com.snackgame.server.support.general.DatabaseCleaningDataJpaTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@DatabaseCleaningDataJpaTest
class BestScoresTest {

    @Autowired
    private lateinit var bestScores: BestScores

    @BeforeEach
    fun setUp() {
        BestScoreFixture.saveAll()
    }

    @Test
    fun `높은 점수 순으로 50개의 랭킹을 찾아온다`() {
        assertThat(bestScores.rankLeadersBy(50, Metadata.APPLE_GAME.gameId, null))
            .extracting("ownerId")
            .containsExactly(
                사과게임_시즌1_땡칠_20점().ownerId,
                사과게임_시즌1_정환_20점().ownerId,
                사과게임_시즌1_유진_20점().ownerId,
                사과게임_베타시즌_똥수_10점().ownerId,
                사과게임_베타시즌_땡칠_10점().ownerId,
                사과게임_베타시즌_정환_8점().ownerId,
                사과게임_베타시즌_정언_8점().ownerId,
                사과게임_시즌1_정언_8점().ownerId,
                사과게임_베타시즌_유진_6점().ownerId
            )
    }

    @Test
    fun `점수가 같으면 같은 순위로 가져온다`() {
        val ranks = bestScores.rankLeadersBy(50, Metadata.APPLE_GAME.gameId, null)

        assertThat(ranks[3].ownerId).isEqualTo(사과게임_베타시즌_똥수_10점().ownerId)
        assertThat(ranks[3].score).isEqualTo(사과게임_베타시즌_똥수_10점().score)
        assertThat(ranks[4].ownerId).isEqualTo(사과게임_베타시즌_땡칠_10점().ownerId)
        assertThat(ranks[4].score).isEqualTo(사과게임_베타시즌_땡칠_10점().score)

        assertThat(ranks[3].rank).isEqualTo(ranks[4].rank)
    }

    @Test
    fun `공동1등 2명 다음은 3등이다`() {
        assertThat(bestScores.rankLeadersBy(50, Metadata.APPLE_GAME.gameId, null))
            .extracting("rank", "score")
            .containsSubsequence(
                tuple(1L, 20),
                tuple(1L, 20),
                tuple(1L, 20),
                tuple(4L, 10)
            )
    }

    @Test
    fun `사용자의 최고점수 랭킹을 가져온다`() {
        val rank = bestScores.findRankOf(MemberFixture.땡칠().id, Metadata.APPLE_GAME.gameId, null)

        assertThat(rank!!.rank).isEqualTo(1)
        assertThat(rank)
            .usingRecursiveComparison()
            .comparingOnlyFields("score", "ownerId")
            .isEqualTo(사과게임_시즌1_땡칠_20점())
    }
}
