@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.history.dao

import com.snackgame.server.history.fixture.SnackgameFixture
import com.snackgame.server.member.fixture.MemberFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.support.fixture.FixtureSaver
import com.snackgame.server.support.general.DatabaseCleaningDataJpaTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import java.time.LocalDate

@DisplayNameGeneration(ReplaceUnderscores::class)
@DatabaseCleaningDataJpaTest
class SnackgameHistoryDaoTest {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    private lateinit var snackgameHistoryDao: SnackgameHistoryDao


    @BeforeEach
    fun setUp() {
        MemberFixture.saveAll()
        SnackgameFixture.saveAll()
        this.snackgameHistoryDao = SnackgameHistoryDao(jdbcTemplate)
    }

    @Test
    fun `최근 25게임의 전적을 조회할 수 있다`() {
        repeat(25) { FixtureSaver.save(SnackgameFixture.eighth()) }

        val scores = snackgameHistoryDao.selectBySession(땡칠().id, 25)
            .map { it.score }

        assertThat(scores).containsOnly(800)
    }

    @Test
    fun `전적을 기간으로 조회할 때는 각 날짜 별 최고점수를 기준으로 한다`() {
        val baseDate = LocalDate.now().minusDays(3).plusDays(1)

        assertThat(snackgameHistoryDao.selectByDate(땡칠().id, baseDate)).hasSize(3)
    }

    @Test
    fun `전적을 기간으로 조회할 때 최고점수보다 낮은 점수는 고려되지 않는다`() {
        val scores = snackgameHistoryDao.selectByDate(땡칠().id)
            .map { it.score }

        assertThat(scores).doesNotContain(SnackgameFixture.second().score, SnackgameFixture.sixth().score)
    }

    @Test
    fun `전적을 기간으로 조회할 때는 최신날짜부터 조회한다`() {
        val scores = snackgameHistoryDao.selectByDate(땡칠().id)
            .map { it.score }

        assertThat(scores).doesNotContain(SnackgameFixture.first().score)
    }
}
