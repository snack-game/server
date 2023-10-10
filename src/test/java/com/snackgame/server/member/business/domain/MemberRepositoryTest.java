package com.snackgame.server.member.business.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.snackgame.server.member.business.exception.MemberNotFoundException;
import com.snackgame.server.member.fixture.MemberFixture;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp(@Autowired TestEntityManager entityManager) {
        MemberFixture.persistAllWith(entityManager);
    }

    @Test
    void 사용자를_없는_id로_찾으면_예외를_던진다() {
        assertThatThrownBy(() -> memberRepository.getById(999L))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 사용자를_없는_이름으로_찾으면_예외를_던진다() {
        assertThatThrownBy(() -> memberRepository.getByName(new Name("없는이름")))
                .isInstanceOf(MemberNotFoundException.class);
    }
}
