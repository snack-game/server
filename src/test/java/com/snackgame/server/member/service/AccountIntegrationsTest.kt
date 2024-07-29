package com.snackgame.server.member.service

import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.IllegalTransactionStateException

@Suppress("NonAsciiCharacters")
@ServiceTest
class AccountIntegrationsTest {

    @Autowired
    private lateinit var accountIntegrations: List<AccountIntegration>


    @Test
    fun `계정 통합 구현체는 독립적으로 실행할 수 없다`() {
        assertSoftly { softly ->
            accountIntegrations.forEach {
                softly.assertThatThrownBy { it.execute(1, 2) }
                    .isInstanceOf(IllegalTransactionStateException::class.java)
                    .hasMessage("No existing transaction found for transaction marked with propagation 'mandatory'")
            }
        }
    }
}
