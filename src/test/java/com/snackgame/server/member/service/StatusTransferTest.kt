package com.snackgame.server.member.service

import com.snackgame.server.member.domain.MemberRepository
import com.snackgame.server.member.fixture.MemberFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.member.fixture.MemberFixture.똥수
import com.snackgame.server.support.fixture.FixtureSaver
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Suppress("NonAsciiCharacters")
@ServiceTest
class StatusTransferTest {

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var statusTransfer: StatusTransfer

    @BeforeEach
    fun setUp() {
        MemberFixture.saveAll()
    }

    @Transactional
    @Test
    fun `한 사용자의 경험치를 다른 사용자에게 이전한다`() {
        FixtureSaver.save(땡칠().also { it.status.addExp(123.0) })
        FixtureSaver.save(똥수().also { it.status.addExp(123.0) })

        statusTransfer.execute(땡칠().id, 똥수().id)

        assertThat(memberRepository.findById(똥수().id))
            .map { it.status.totalExp }.get()
            .isEqualTo(246.0)
    }
}
