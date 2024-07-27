package com.snackgame.server.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.snackgame.server.member.exception.NameLengthException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class NameTest {

    @Test
    void 이름이_2글자보다_짧으면_예외를_던진다() {
        assertThatThrownBy(() -> new Name("1"))
                .isInstanceOf(NameLengthException.class);
    }

    @Test
    void 이름이_16글자보다_길면_예외를_던진다() {
        assertThatThrownBy(() -> new Name("123456789abcdfegh"))
                .isInstanceOf(NameLengthException.class);
    }

    @Test
    void 이름이_2글자_이상_16글자_이하_이면_잘_생성된다() {
        assertThatNoException()
                .isThrownBy(() -> new Name("2자"));
    }

    @Nested
    class 현재_이름대신_가능한_이름을_생성한다 {

        @Test
        void 숫자가_안붙은_경우_2를_붙인다() {
            var numbered = new Name("땡칠");

            assertThat(numbered.nextAvailable().getString()).isEqualTo("땡칠_2");
        }

        @Test
        void 숫자가_이미_붙은_경우_다음_숫자를_붙인다() {
            var numbered = new Name("땡칠_21243");

            assertThat(numbered.nextAvailable().getString()).isEqualTo("땡칠_21244");
        }
    }
}
