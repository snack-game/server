package com.snackgame.server.member.fixture;

import com.snackgame.server.member.business.domain.Member;

@SuppressWarnings("NonAsciiCharacters")
public class MemberFixture {

    public static Member 똥수() {
        return new Member(1L, "똥수", "홍천고등학교");
    }

    public static Member 땡칠() {
        return new Member(2L, "땡칠", "우테코");
    }
}
