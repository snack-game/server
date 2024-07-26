@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.member.fixture

import com.snackgame.server.member.controller.dto.NameRequest
import com.snackgame.server.member.domain.Member
import com.snackgame.server.member.domain.Name
import com.snackgame.server.member.domain.SocialMember
import com.snackgame.server.member.fixture.GroupFixture.숭실대학교
import com.snackgame.server.member.fixture.GroupFixture.우테코
import com.snackgame.server.member.fixture.GroupFixture.홍천고등학교
import com.snackgame.server.support.fixture.FixtureSaver

// TODO: refactor to companion object
object MemberFixture {
    @JvmStatic
    fun 똥수(): Member {
        return Member(Name("똥수"), 홍천고등학교(), id = 1)
    }

    @JvmStatic
    fun 똥수_인증정보(): NameRequest {
        return NameRequest(똥수().getNameAsString())
    }

    @JvmStatic
    fun 땡칠(): Member {
        return Member(Name("땡칠"), 우테코(), id = 2)
    }

    @JvmStatic
    fun 땡칠_인증정보(): NameRequest {
        return NameRequest(땡칠().getNameAsString())
    }

    @JvmStatic
    fun 정환(): SocialMember {
        return SocialMember(
            "GOOGLE", "user123412341234",
            Name("정환"),
            id = 3
        )
    }

    @JvmStatic
    fun 유진(): Member {
        return Member(Name("유진"), 숭실대학교(), id = 4)
    }

    @JvmStatic
    fun 정언(): Member {
        return Member(Name("정언"), 숭실대학교(), id = 5)
    }

    @JvmStatic
    fun saveAll() {
        GroupFixture.saveAll()
        FixtureSaver.save(
            똥수(),
            땡칠(),
            정환(),
            유진(),
            정언()
        )
    }
}
