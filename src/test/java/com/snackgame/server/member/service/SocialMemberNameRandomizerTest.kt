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

    @Test
    fun `이름은 정해둔 배열에 존재하는 단어만을 가지고 생성되어야한다`() {
        var randomizedPureWord = socialMemberNameRandomizer.getName().string.removeSuffix("_스낵이")

        var list: MutableList<String> = mutableListOf()
        randomizedPureWord.split("_").forEach {
            list.add(it)
        }

        assertThat(SocialMemberNameRandomizer.determiners).contains(list[0])
        assertThat(SocialMemberNameRandomizer.adjectives).contains(list[1])

    }


}