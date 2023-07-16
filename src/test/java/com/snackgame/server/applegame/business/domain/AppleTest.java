package com.snackgame.server.applegame.business.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.snackgame.server.applegame.business.exception.AppleNumberRangeException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AppleTest {

    @Test
    void 범위_밖_숫자는_예외를_던진다() {
        assertThatThrownBy(() -> new Apple(0))
                .isInstanceOf(AppleNumberRangeException.class);
        assertThatThrownBy(() -> new Apple(10))
                .isInstanceOf(AppleNumberRangeException.class);
    }

    @Test
    void 범위안의_랜덤_숫자를_생성한다() {
        assertThatNoException()
                .isThrownBy(Apple::ofRandomizedNumber);
    }

    @Test
    void 빈_사과인지_알_수_있다() {
        assertThat(Apple.EMPTY.isEmpty()).isTrue();
    }
}
