package com.snackgame.server.member.service

import com.snackgame.server.member.exception.MemberNotFoundException
import com.snackgame.server.member.fixture.MemberFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent
import org.springframework.boot.test.mock.mockito.MockBean

@Suppress("NonAsciiCharacters")
@ServiceTest
class MemberWithdrawalTest {

    @Autowired
    private lateinit var memberAccountService: MemberAccountService

    @MockBean
    private lateinit var mockOperation: MockOperation

    @TestComponent
    class MockOperation : MemberWithdrawalOperation {
        override fun executeOn(memberId: Long) {
        }
    }

    @Test
    fun `회원 탈퇴를 할 수 있다`() {
        MemberFixture.saveAll()

        memberAccountService.delete(땡칠().id)

        assertThatThrownBy { memberAccountService.getBy(땡칠().id) }
            .isInstanceOf(MemberNotFoundException::class.java)
    }

    @Test
    fun `회원 탈퇴에 필요한 절차들을 수행한다`() {
        MemberFixture.saveAll()

        memberAccountService.delete(땡칠().id)

        verify(mockOperation, times(1)).executeOn(anyLong())
    }
}
