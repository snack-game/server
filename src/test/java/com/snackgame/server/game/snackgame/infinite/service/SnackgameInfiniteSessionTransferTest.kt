package com.snackgame.server.game.snackgame.infinite.service

import com.snackgame.server.game.snackgame.core.domain.Snackgame
import com.snackgame.server.game.snackgame.infinite.domain.SnackgameInfinite
import com.snackgame.server.game.snackgame.infinite.domain.SnackgameInifiniteRepository
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
class SnackgameInfiniteSessionTransferTest {

    @Autowired
    private lateinit var snackgameInfiniteRepository: SnackgameInifiniteRepository

    @Autowired
    private lateinit var snackgameInfiniteSessionTransfer: SnackgameInfiniteSessionTransfer

    @BeforeEach
    fun setUp() {
        MemberFixture.saveAll()
    }

    @Transactional
    @Test
    fun `한 사용자의 스낵게임 세션을 다른 사용자에게 모두 이전한다`() {
        repeat(5) { FixtureSaver.save(SnackgameInfinite(땡칠().id)) }

        snackgameInfiniteSessionTransfer.execute(땡칠().id, 정환().id)

        val distinctOwnerIds = snackgameInfiniteRepository.findAll()
            .map { it.ownerId }
            .distinct()
        assertThat(distinctOwnerIds).singleElement().isEqualTo(정환().id)
    }
}
