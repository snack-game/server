@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.member.domain

import com.snackgame.server.member.exception.GuestRestrictedException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class GuestTest {

    @Test
    fun `게스트는 이름을 변경할 수 없다`() {
        assertThatThrownBy {
            Guest(Name("게스트")).changeNameTo(Name("이름"))
        }.isInstanceOf(GuestRestrictedException::class.java)
    }

    @Test
    fun `게스트는 그룹을 변경할 수 없다`() {
        assertThatThrownBy {
            Guest(Name("게스트")).changeGroupTo(Group("그룹"))
        }.isInstanceOf(GuestRestrictedException::class.java)
    }

    @Test
    fun `게스트의 계정 타입은 GUEST이다`() {
        assertThat(Guest(Name("게스트")).accountType).isEqualTo(AccountType.GUEST)
    }
}
