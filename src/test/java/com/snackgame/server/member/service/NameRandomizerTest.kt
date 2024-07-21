@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.member.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class NameRandomizerTest {

    private val nameRandomizer = NameRandomizer()
    private val randomizedLength = 10

    @Test
    fun `이름 앞에 접두사 및 '_' 가 붙는다`() {
        val randomized = nameRandomizer.getWith("guest").string

        assertThat(randomized).startsWith("guest_")
    }

    @Test
    fun `'_' 뒤 알파벳 10자리를 무작위로 생성한다`() {
        val randomized = nameRandomizer.getWith("guest").string

        assertThat(randomized.substringAfter('_')).hasSize(randomizedLength)
    }
}
