package com.snackgame.server.auth.oauth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProviderTest {

    @Test
    void 발급자를_식별한다() {
        assertThat(Provider.thatMatches("https://accounts.google.com")).isEqualTo(Provider.GOOGLE);
        assertThat(Provider.thatMatches("https://kauth.kakao.com")).isEqualTo(Provider.KAKAO);
    }
}
