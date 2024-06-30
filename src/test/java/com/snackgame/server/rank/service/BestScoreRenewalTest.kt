@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.rank.service

import com.snackgame.server.fixture.BestScoreFixture
import com.snackgame.server.game.metadata.Metadata
import com.snackgame.server.game.session.event.SessionEndEvent
import com.snackgame.server.member.fixture.MemberFixture
import com.snackgame.server.member.service.MemberAccountService
import com.snackgame.server.rank.domain.BestScores
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@ServiceTest
internal class BestScoreRenewalTest {

    @Autowired
    lateinit var bestScoreRenewal: BestScoreRenewal

    @Autowired
    lateinit var memberAccountService: MemberAccountService

    @Autowired
    lateinit var bestScores: BestScores

    @BeforeEach
    fun setUp() {
        MemberFixture.saveAll()
        BestScoreFixture.saveAll()
    }

    @Test
    fun `게스트 점수는 랭크에 기록되지 않는다`() {
        val guest = memberAccountService.createGuest()
        bestScoreRenewal.renewBestScoreWith(
            SessionEndEvent(
                Metadata.APPLE_GAME,
                guest.id,
                1,
                100,
            )
        )

        assertThat(bestScores.findAll()).filteredOn { it.ownerId == guest.id }.isEmpty()
    }
}
