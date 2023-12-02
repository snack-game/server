package com.snackgame.server.member.domain;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import com.snackgame.server.member.exception.MemberNotFoundException;
import com.snackgame.server.member.fixture.MemberFixture;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp(@Autowired EntityManagerFactory entityManagerFactory) {
        MemberFixture.persistAllUsing(entityManagerFactory);
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

    @Test
    void 제공자와_제공자Id로_소셜_사용자를_찾는다() {
        var found = memberRepository.findByProviderAndProvidedId(땡칠2().getProvider(), 땡칠2().getProvidedId());

        assertThat(found).get()
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(땡칠2());
    }

    @Test
    void 어떤_문자열로_시작하는_멤버들을_찾는다() {
        List<Member> findings = memberRepository.findByNameStringStartingWith("땡");

        assertThat(findings).hasSize(2);
    }
}
