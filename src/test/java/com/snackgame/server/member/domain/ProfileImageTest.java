package com.snackgame.server.member.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.snackgame.server.member.exception.InvalidProfileImageException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProfileImageTest {

    @ParameterizedTest
    @ValueSource(strings = {"http://image-on-web", "https://image-on-web"})
    void 허용된_스키마만_사용할_수_있다(String url) {
        assertThatNoException()
                .isThrownBy(() -> new ProfileImage(url));
    }

    @Test
    void 허용되지_않은_스키마는_예외를_던진다() {
        assertThatThrownBy(() -> new ProfileImage("ftp://image-from-ftp"))
                .isInstanceOf(InvalidProfileImageException.class);
    }
}
