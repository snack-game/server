package com.snackgame.server.member.business.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.snackgame.server.member.business.exception.EmptyNameException;
import com.snackgame.server.member.business.exception.NameLengthException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class NameTest {

    @Test
    void 이름은_비워둘_수_없다() {
        assertThatThrownBy(() -> new Name(null))
                .isInstanceOf(EmptyNameException.class);
    }

    @Test
    void 이름이_2글자보다_짧으면_예외를_던진다() {
        assertThatThrownBy(() -> new Name("1"))
                .isInstanceOf(NameLengthException.class);
    }

    @Test
    void 이름이_2글자_이상이면_잘_생성된다() {
        assertThatNoException()
                .isThrownBy(() -> new Name("2자"));
    }
}
