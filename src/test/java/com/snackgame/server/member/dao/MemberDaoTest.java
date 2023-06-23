package com.snackgame.server.member.dao;

import static com.snackgame.server.member.fixture.MemberFixture.똥수;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.dao.dto.MemberDto;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
class MemberDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        this.memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void 삽입한다() {
        var member = new Member(똥수().getName(), 똥수().getGroup());

        MemberDto inserted = memberDao.insert(MemberDto.of(member));

        assertThat(inserted).isNotNull();
    }

    @Test
    void id로_조회한다() {
        var member = new Member(똥수().getName(), 똥수().getGroup());
        MemberDto inserted = memberDao.insert(MemberDto.of(member));

        assertThat(memberDao.selectBy(inserted.getId()))
                .get()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(MemberDto.of(똥수()));
    }

    @Test
    void 이름으로_조회한다() {
        var member = new Member(똥수().getName(), 똥수().getGroup());
        memberDao.insert(MemberDto.of(member));

        MemberDto found = memberDao.selectBy(member.getName()).get();
        assertThat(found.getName()).isEqualTo(똥수().getName());
    }

    @Test
    void 업데이트한다() {
        var member = new Member(똥수().getName(), 똥수().getGroup());
        MemberDto inserted = memberDao.insert(MemberDto.of(member));

        member.changeNameTo("똥똥수");
        memberDao.update(MemberDto.of(member));

        assertThat(memberDao.selectBy(inserted.getId())).get()
                .usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(inserted);
    }
}
