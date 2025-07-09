@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.game.snackgame.core.domain

import com.snackgame.server.game.snackgame.exception.NegativeItemCountException
import com.snackgame.server.game.snackgame.fixture.ItemFixture.똥수_폭탄
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ItemTest {

    @Test
    fun `아이템 개수가 음수이면 사용할 수 없다`() {
        assertThatThrownBy { 똥수_폭탄().useOne() }.isInstanceOf(NegativeItemCountException::class.java)
    }
}