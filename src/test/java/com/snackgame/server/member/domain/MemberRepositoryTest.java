package com.snackgame.server.member.domain;

import static com.snackgame.server.member.fixture.MemberFixture.정환;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.member.exception.MemberNotFoundException;
import com.snackgame.server.member.fixture.MemberFixture;
import com.snackgame.server.support.general.DatabaseCleaningDataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DatabaseCleaningDataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        MemberFixture.saveAll();
    }

    @Test
    void 사용자를_없는_id로_찾으면_예외를_던진다() {
        assertThatThrownBy(() -> memberRepository.getById(0L))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 사용자를_없는_이름으로_찾으면_예외를_던진다() {
        assertThatThrownBy(() -> memberRepository.getByName(new Name("없는이름")))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 제공자와_제공자Id로_소셜_사용자를_찾는다() {
        var found = memberRepository.findByProviderAndProvidedId(정환().getProvider(), 정환().getProvidedId());

        assertThat(found).get()
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(정환());
    }

    @Test
    void 어떤_문자열로_시작하는_멤버들을_찾는다() {
        List<Member> findings = memberRepository.findByNameStringStartingWith("정");

        assertThat(findings).hasSize(2);
    }
}
