package com.snackgame.server.member.fixture;

import static com.snackgame.server.member.fixture.GroupFixture.숭실대학교;
import static com.snackgame.server.member.fixture.GroupFixture.우테코;
import static com.snackgame.server.member.fixture.GroupFixture.홍천고등학교;

import com.snackgame.server.member.domain.Member;
import com.snackgame.server.member.domain.Name;
import com.snackgame.server.member.domain.SocialMember;
import com.snackgame.server.support.fixture.FixtureSaver;

@SuppressWarnings("NonAsciiCharacters")
public class MemberFixture {

    public static Member 똥수() {
        return new Member(1L, new Name("똥수"), 홍천고등학교());
    }

    public static Member 땡칠() {
        return new Member(2L, new Name("땡칠"), 우테코());
    }

    public static SocialMember 땡칠2() {
        return SocialMember.from(
                new Member(3L, new Name("땡칠2"), null),
                "GOOGLE", "user123412341234"
        );
    }

    public static Member 시연() {
        return new Member(4L, new Name("시연"), 숭실대학교());
    }

    public static Member 주호() {
        return new Member(5L, new Name("주호"), 숭실대학교());
    }

    public static void saveAll() {
        GroupFixture.saveAll();
        FixtureSaver.save(
                똥수(),
                땡칠(),
                땡칠2(),
                시연(),
                주호()
        );
    }
}
