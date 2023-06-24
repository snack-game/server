package com.snackgame.server.member.dao;

import static com.snackgame.server.member.fixture.MemberFixture.우테코;
import static com.snackgame.server.member.fixture.MemberFixture.홍천고등학교;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.snackgame.server.member.business.domain.Group;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
class GroupDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    private GroupDao groupDao;

    @BeforeEach
    void setUp() {
        this.groupDao = new GroupDao(jdbcTemplate);
    }

    @Test
    void 삽입한다() {
        var group = 홍천고등학교();

        Group inserted = groupDao.insert(group);

        assertThat(inserted).isNotNull();
    }

    @Test
    void id로_조회한다() {
        var group = 홍천고등학교();
        Group inserted = groupDao.insert(group);

        assertThat(groupDao.selectBy(inserted.getId()))
                .get()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(홍천고등학교());
    }

    @Test
    void 이름으로_조회한다() {
        var group = 우테코();
        groupDao.insert(group);

        Group found = groupDao.selectBy(group.getName()).get();
        assertThat(found)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(우테코());
    }

    @Test
    void 특정_이름으로_시작하는_그룹들을_찾는다() {
        Group fullName = groupDao.insert(new Group("숭실대학교"));
        Group shortName = groupDao.insert(new Group("숭실대"));

        assertThat(groupDao.selectByNameLike("숭실대"))
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(fullName, shortName);
    }
}
