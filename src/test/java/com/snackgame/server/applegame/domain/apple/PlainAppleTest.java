package com.snackgame.server.applegame.domain.apple;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.snackgame.server.applegame.exception.AppleNumberRangeException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PlainAppleTest {

    @Test
    void 존재한다() {
        var plainApple = PlainApple.of(9);

        assertThat(plainApple.exists()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10})
    void 잘못된_범위의_숫자로는_사과를_만들_수_없다(int value) {
        assertThatThrownBy(() -> PlainApple.of(value))
                .isInstanceOf(AppleNumberRangeException.class);
    }

    @Test
    void 자신을_황금사과로_바꾼다() {
        int number = 9;
        var plainApple = PlainApple.of(number);

        assertThat(plainApple.golden()).isEqualTo(GoldenApple.of(number));
    }

    @Test
    void 황금사과가_아니다() {
        var plainApple = PlainApple.of(9);

        assertThat(plainApple.isGolden()).isFalse();
    }

    @RepeatedTest(50)
    void 사과를_랜덤_생성한다() {
        assertThatNoException()
                .isThrownBy(PlainApple::ofRandomizedNumber);
    }
}
