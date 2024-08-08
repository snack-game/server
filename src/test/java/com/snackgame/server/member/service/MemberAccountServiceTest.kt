@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.member.service

import com.snackgame.server.member.exception.DuplicateNameException
import com.snackgame.server.member.exception.MemberNotFoundException
import com.snackgame.server.member.fixture.MemberFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.member.fixture.MemberFixture.똥수
import com.snackgame.server.member.service.dto.MemberDetailsResponse
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@DisplayNameGeneration(ReplaceUnderscores::class)
@ServiceTest
internal class MemberAccountServiceTest {
    @Autowired
    private lateinit var memberAccountService: MemberAccountService

    @Test
    fun `이름과 그룹이름으로 사용자를 생성한다`() {
        val created = memberAccountService.createWith(똥수().getNameAsString(), 똥수().group!!.name)

        assertThat(memberAccountService.getBy(created.id))
            .usingRecursiveComparison()
            .comparingOnlyFields("id", "name", "group.name")
            .isEqualTo(MemberDetailsResponse.of(똥수()))
    }

    @Test
    fun `그룹 없이 이름만으로 사용자를 생성한다`() {
        val expected = 똥수().also { it.changeGroupTo(null) }
        val created = memberAccountService.createWith(똥수().getNameAsString(), null)

        assertThat(memberAccountService.getBy(created.id))
            .usingRecursiveComparison()
            .comparingOnlyFields("id", "name", "group.name")
            .isEqualTo(MemberDetailsResponse.of(expected))
    }

    @Test
    fun `임시사용자를 생성한다`() {
        val guest = memberAccountService.createGuest()

        val created = memberAccountService.getBy(guest.id)

        assertThat(created.id).isNotNull()
        assertThat(created.name).startsWith("guest")
    }

    @Test
    fun `임시사용자 이름이 중복이면 다시 만든다`() {
        val existing = memberAccountService.createWith("guest_abc")

        val guest = memberAccountService.createGuest()

        assertThat(guest.name).isNotEqualTo(existing.name)
    }

    @Test
    fun `중복된 이름으로 생성할 수 없다`() {
        memberAccountService.createWith(똥수().getNameAsString())

        assertThatThrownBy { memberAccountService.createWith(똥수().getNameAsString(), null) }
            .isInstanceOf(DuplicateNameException::class.java)
            .hasMessage("이미 존재하는 이름입니다")
    }

    @Test
    fun `사용자 이름을 수정한다`() {
        val member = memberAccountService.createWith(똥수().getNameAsString())

        memberAccountService.changeNameOf(member.id, "똥똥수")

        val found = memberAccountService.getBy(member.id)
        assertThat(found.name).isEqualTo("똥똥수")
    }

    @Test
    fun `중복된 이름으로 수정할 수 없다`() {
        memberAccountService.createWith(땡칠().getNameAsString())
        val member = memberAccountService.createWith(똥수().getNameAsString())

        assertThatThrownBy { memberAccountService.changeNameOf(member.id, 땡칠().getNameAsString()) }
            .isInstanceOf(DuplicateNameException::class.java)
            .hasMessage("이미 존재하는 이름입니다")
    }

    @Test
    fun `그룹 이름을 수정한다`() {
        val member = memberAccountService.createWith(똥수().getNameAsString(), 똥수().group!!.name)
        memberAccountService.changeGroupNameOf(member.id, "홍천고")

        assertThat(memberAccountService.getBy(member.id).group!!.name)
            .isEqualTo("홍천고")
    }

    @Test
    fun `사용자를 id로 찾는다`() {
        val created = memberAccountService.createWith(땡칠().getNameAsString(), 땡칠().group!!.name)

        val found = memberAccountService.getBy(created.id)

        assertThat(found)
            .usingRecursiveComparison()
            .comparingOnlyFields("name", "group.name")
            .isEqualTo(MemberDetailsResponse.of(땡칠()))
    }

    @Test
    fun `사용자를 없는 id로 찾으면 예외를 던진다`() {
        memberAccountService.createWith(땡칠().getNameAsString())

        assertThatThrownBy { memberAccountService.getBy(999L) }
            .isInstanceOf(MemberNotFoundException::class.java)
    }

    @Test
    fun `사용자를 없는 이름으로 찾으면 예외를 던진다`() {
        memberAccountService.createWith(땡칠().getNameAsString())

        assertThatThrownBy { memberAccountService.getBy(똥수().getNameAsString()) }
            .isInstanceOf(MemberNotFoundException::class.java)
    }

    @Test
    fun `특정 이름으로 시작하는 사용자 이름들을 찾는다`() {
        val fullName = memberAccountService.createWith("땡칠이").name
        val shortName = memberAccountService.createWith("땡칠").name

        assertThat(memberAccountService.findNamesStartWith("땡칠"))
            .contains(fullName, shortName)
    }

    @Test
    fun `계정 통합 시 기존 계정은 제거된다`() {
        MemberFixture.saveAll()
        val guest = memberAccountService.createGuest()

        memberAccountService.integrate(guest.id, 땡칠().id)

        assertThatThrownBy { memberAccountService.getBy(guest.id) }
            .isInstanceOf(MemberNotFoundException::class.java)
    }
}
