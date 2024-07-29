package com.snackgame.server.game.snackgame.service

import com.snackgame.server.game.snackgame.domain.Snackgame
import com.snackgame.server.game.snackgame.domain.SnackgameRepository
import com.snackgame.server.member.fixture.MemberFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.member.fixture.MemberFixture.정환
import com.snackgame.server.support.fixture.FixtureSaver
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Suppress("NonAsciiCharacters")
@ServiceTest
class SnackgameSessionTransferTest {

    @Autowired
    private lateinit var snackgameRepository: SnackgameRepository

    @Autowired
    private lateinit var snackgameSessionTransfer: SnackgameSessionTransfer

    @BeforeEach
    fun setUp() {
        MemberFixture.saveAll()
    }

    @Transactional
    @Test
    fun `한 사용자의 스낵게임 세션을 다른 사용자에게 모두 이전한다`() {
        repeat(5) { FixtureSaver.save(Snackgame(땡칠().id)) }

        snackgameSessionTransfer.execute(땡칠().id, 정환().id)

        val distinctOwnerIds = snackgameRepository.findAll()
            .map { it.ownerId }
            .distinct()
        assertThat(distinctOwnerIds).singleElement().isEqualTo(정환().id)
    }
}
