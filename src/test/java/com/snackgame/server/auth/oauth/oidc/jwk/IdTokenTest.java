package com.snackgame.server.auth.oauth.oidc.jwk;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.snackgame.server.auth.exception.AuthException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class IdTokenTest {

    private static final String EXPIRED_ID_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjY3MTk2NzgzNTFhNWZhZWRjMmU3MDI3NGJiZWE2MmRhMmE4YzRhMTIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTA5NTk4MzI3ODM2NzMwNDE0MDMiLCJlbWFpbCI6ImdvcmFlMDJAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJWbmVzOWZtTGxCV3BsZ2p5NG81cDdRIiwiaWF0IjoxNzE2OTcwMjE4LCJleHAiOjE3MTY5NzM4MTh9.IyWIGRprPTO7u_LwpsPPxGQ6_yNAjY9XzysBoR9vifuD0j9-zuhdGQKNUvLjiNVKoG1cW0FPag1iR-iSWhJHx8jIfoGZ2yr6gK2PUcn15B8MTF7PxVAquRrDhPxvpsl_QZnHVAcF33sOPeYMr_aQKLGVYMoHVSOLizAPC5TNBipePPjKjlErnfYOEiHGzIix_o2E7AgyKoJY71Mnc73bgm0VvA7mnznm-tIHn5An1-YC6lC6JvO4_vgGlh3ezmw0902oYx5iw3taACl3F6k_IxLJGStowyzCvR1eS7ZcLFWU6goHBv5j7117WJHimNaRnfyvtYJybqHE32xMYhGT-Q";

    @ParameterizedTest
    @ValueSource(strings = {"", "header", "header.body", "header.body.signature.else"})
    void 세_부분으로_이뤄져야_한다(String idToken) {
        assertThatThrownBy(() -> new IdToken(idToken))
                .isInstanceOf(AuthException.class)
                .hasMessage("토큰이 유효하지 않습니다");
    }

    @Test
    void JWT_발급자를_알_수_있다() {
        IdToken idToken = new IdToken(EXPIRED_ID_TOKEN);

        assertThat(idToken.getIss()).isEqualTo("https://accounts.google.com");
    }

    @Test
    void JWT_KeyId를_알_수_있다() {
        IdToken idToken = new IdToken(EXPIRED_ID_TOKEN);

        assertThat(idToken.getKeyId()).isEqualTo("6719678351a5faedc2e70274bbea62da2a8c4a12");
    }

    @Test
    void JWT_Audience를_알_수_있다() {
        IdToken idToken = new IdToken(EXPIRED_ID_TOKEN);

        assertThat(idToken.getAudience()).isEqualTo("407408718192.apps.googleusercontent.com");
    }
}
