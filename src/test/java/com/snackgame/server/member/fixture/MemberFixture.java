package com.snackgame.server.member.fixture;

import com.snackgame.server.member.business.domain.Group;
import com.snackgame.server.member.business.domain.Member;

@SuppressWarnings("NonAsciiCharacters")
public class MemberFixture {

    public static Group 홍천고등학교() {
        return new Group(1L, "홍천고등학교");
    }

    public static Group 우테코() {
        return new Group(2L, "우테코");
    }

    public static Member 똥수() {
        return new Member(1L, "똥수", 홍천고등학교());
    }

    public static Member 땡칠() {
        return new Member(2L, "땡칠", 우테코());
    }
}
