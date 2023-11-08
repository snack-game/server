package com.snackgame.server.member.fixture;

import static com.snackgame.server.member.fixture.FixtureUtil.idIgnored;
import static com.snackgame.server.member.fixture.FixtureUtil.pushing;

import javax.persistence.EntityManagerFactory;

import com.snackgame.server.member.business.domain.Group;
import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.business.domain.Name;
import com.snackgame.server.member.business.domain.SocialMember;

@SuppressWarnings("NonAsciiCharacters")
public class MemberFixture {

    public static Group 홍천고등학교() {
        return new Group(1L, "홍천고등학교");
    }

    public static Group 우테코() {
        return new Group(2L, "우테코");
    }

    public static Group 숭실대학교() {
        return new Group(3L, "숭실대학교");
    }

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

    public static void persistAllUsing(EntityManagerFactory entityManagerFactory) {
        var entityManager = entityManagerFactory.createEntityManager();
        var transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(idIgnored(홍천고등학교()));
        entityManager.persist(idIgnored(우테코()));
        entityManager.persist(idIgnored(숭실대학교()));

        entityManager.persist(pushing(홍천고등학교(), idIgnored(똥수())));
        entityManager.persist(pushing(우테코(), idIgnored(땡칠())));
        entityManager.persist(idIgnored(땡칠2()));
        entityManager.persist(pushing(숭실대학교(), idIgnored(시연())));
        entityManager.persist(pushing(숭실대학교(), idIgnored(주호())));
        transaction.commit();
    }
}
