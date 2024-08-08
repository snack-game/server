@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.status

import com.snackgame.server.member.fixture.MemberFixture
import com.snackgame.server.member.fixture.MemberFixture.정환
import com.snackgame.server.member.service.MemberAccountService
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@ServiceTest
class StatusWithdrawalTest {

    @Autowired
    private lateinit var memberAccountService: MemberAccountService

    @Autowired
    private lateinit var statusWithdrawal: StatusWithdrawal

    @BeforeEach
    fun setUp() {
        MemberFixture.saveAll()
    }

    @Transactional
    @Test
    fun `회원의 레벨과 경험치를 초기화한다`() {
        statusWithdrawal.executeOn(정환().id)

        assertThat(memberAccountService.getBy(정환().id).status.level).isZero()
        assertThat(memberAccountService.getBy(정환().id).status.exp).isZero()
    }
}
