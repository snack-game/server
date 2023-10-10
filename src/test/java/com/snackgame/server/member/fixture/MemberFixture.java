package com.snackgame.server.member.fixture;

import static com.snackgame.server.member.fixture.FixtureUtil.idIgnored;
import static com.snackgame.server.member.fixture.FixtureUtil.pushing;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.snackgame.server.member.business.domain.Group;
import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.business.domain.Name;

@SuppressWarnings("NonAsciiCharacters")
public class MemberFixture {

    public static Group 홍천고등학교() {
        return new Group(1L, "홍천고등학교");
    }

    public static Group 우테코() {
        return new Group(2L, "우테코");
    }

    public static Member 똥수() {
        return new Member(1L, new Name("똥수"), 홍천고등학교());
    }

    public static Member 땡칠() {
        return new Member(2L, new Name("땡칠"), 우테코());
    }
    
    public static void persistAllWith(TestEntityManager entityManager) {
        var 홍천고등학교 = entityManager.persist(idIgnored(홍천고등학교()));
        var 우테코 = entityManager.persist(idIgnored(우테코()));

        entityManager.persist(pushing(홍천고등학교, idIgnored(똥수())));
        entityManager.persist(pushing(우테코, idIgnored(땡칠())));
    }
}
