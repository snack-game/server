package com.snackgame.server.applegame.domain.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.snackgame.server.applegame.exception.InaccuratePercentileException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PercentileTest {

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.1, 0.9999, 1.0})
    void 백분위는_0과_1사이의_소수다(double percentile) {
        assertThatNoException()
                .isThrownBy(() -> new Percentile(percentile));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.001, 1.001})
    void 백분위가_0과_1사이여야한다(double percentile) {
        assertThatThrownBy(() -> new Percentile(percentile))
                .isInstanceOf(InaccuratePercentileException.class);
    }

    @ParameterizedTest
    @CsvSource({"0.0, 0", "0.4949, 49", "0.495, 49", "1.0, 100"})
    void 백분위의_정수_이하를_버려_백분율을_구한다(double percentile, int expectedPercentage) {
        int percentage = new Percentile(percentile).percentage();

        assertThat(percentage).isEqualTo(expectedPercentage);
    }
}
