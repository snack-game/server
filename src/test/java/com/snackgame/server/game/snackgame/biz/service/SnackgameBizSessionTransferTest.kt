package com.snackgame.server.game.snackgame.biz.service

import com.snackgame.server.game.snackgame.biz.domain.SnackgameBiz
import com.snackgame.server.game.snackgame.biz.domain.SnackgameBizRepository
import com.snackgame.server.game.snackgame.biz.domain.SnackgameBizSessionTransfer
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
class SnackgameBizSessionTransferTest {

    @Autowired
    private lateinit var snackgameBizRepository: SnackgameBizRepository

    @Autowired
    private lateinit var snackgameBizSessionTransfer: SnackgameBizSessionTransfer

    @BeforeEach
    fun setUp() {
        MemberFixture.saveAll()
    }

    @Transactional
    @Test
    fun `한 사용자의 스낵게임 세션을 다른 사용자에게 모두 이전한다`() {
        repeat(5) { FixtureSaver.save(SnackgameBiz(땡칠().id)) }

        snackgameBizSessionTransfer.execute(땡칠().id, 정환().id)

        val distinctOwnerIds = snackgameBizRepository.findAll()
            .map { it.ownerId }
            .distinct()
        assertThat(distinctOwnerIds).singleElement().isEqualTo(정환().id)
    }
}
