package com.snackgame.server.applegame.business.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.snackgame.server.applegame.business.exception.EmptyAppleException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EmptyAppleTest {

    @Test
    void 비어있다() {
        var emptyApple = EmptyApple.get();

        assertThat(emptyApple.exists()).isFalse();
        assertThat(emptyApple.isEmpty()).isTrue();
    }

    @Test
    void 황금사과가_아니다() {
        var emptyApple = EmptyApple.get();

        assertThat(emptyApple.isGolden()).isFalse();
    }

    @Test
    void 황금사과로_바꿀_수_없다() {
        var emptyApple = EmptyApple.get();

        assertThatThrownBy(() -> emptyApple.golden())
                .isInstanceOf(EmptyAppleException.class)
                .hasMessage("빈 사과를 바꿀 수 없습니다");
    }
}
