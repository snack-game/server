package com.snackgame.server.member.fixture;

import com.snackgame.server.member.domain.Group;
import com.snackgame.server.support.fixture.FixtureSaver;

@SuppressWarnings("NonAsciiCharacters")
public class GroupFixture {

    public static Group 홍천고등학교() {
        return new Group(1L, "홍천고등학교");
    }

    public static Group 우테코() {
        return new Group(2L, "우테코");
    }

    public static Group 숭실대학교() {
        return new Group(3L, "숭실대학교");
    }

    public static void saveAll() {
        FixtureSaver.save(
                홍천고등학교(),
                우테코(),
                숭실대학교()
        );
    }
}
