package com.snackgame.server.member.business.domain;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberTest {

    @Test
    void 일반_회원의_계정_타입은_SELF이다() {
        assertThat(땡칠().getAccountType()).isEqualTo(AccountType.SELF);
    }
}
