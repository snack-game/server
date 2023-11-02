package com.snackgame.server.applegame.business.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.snackgame.server.applegame.business.exception.AppleNumberRangeException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GoldenAppleTest {

    @Test
    void 비어있지_않다() {
        var goldenApple = GoldenApple.of(9);

        assertThat(goldenApple.exists()).isTrue();
        assertThat(goldenApple.isEmpty()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10})
    void 잘못된_범위의_숫자로는_사과를_만들_수_없다(int value) {
        assertThatThrownBy(() -> GoldenApple.of(value))
                .isInstanceOf(AppleNumberRangeException.class);
    }

    @Test
    void 황금사과로_바꿀때_자신을_반환한다() {
        var goldenApple = GoldenApple.of(9);

        assertThat(goldenApple.golden()).isEqualTo(goldenApple);
    }

    @Test
    void 황금사과이다() {
        var goldenApple = GoldenApple.of(9);

        assertThat(goldenApple.isGolden()).isTrue();
    }
}
