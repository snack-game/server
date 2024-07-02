package com.snackgame.server.rank.service

import com.snackgame.server.game.metadata.Metadata.SNACK_GAME
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.rank.exception.NotRankedYetException
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Suppress("NonAsciiCharacters")
@ServiceTest
class BestScoreRankServiceTest {

    @Autowired
    lateinit var bestScoreRankService: BestScoreRankService

    @Test
    fun `자신의 랭크가 없으면 예외를 던진다`() {
        assertThatThrownBy {
            bestScoreRankService.rank(땡칠().id, SNACK_GAME.gameId)
        }.isInstanceOf(NotRankedYetException::class.java)
    }
}
