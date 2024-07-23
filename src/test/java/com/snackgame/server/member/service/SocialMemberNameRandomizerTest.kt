@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.member.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SocialMemberNameRandomizerTest {

    private val socialMemberNameRandomizer = SocialMemberNameRandomizer()

    @Test
    fun `이름은_'_스낵이'로 끝나야_한다`() {
        val randomized = socialMemberNameRandomizer.getName().string

        assertThat(randomized).endsWith("_스낵이")
    }


}