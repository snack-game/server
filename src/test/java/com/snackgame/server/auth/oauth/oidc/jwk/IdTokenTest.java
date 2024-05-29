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

    private static final String EXPIRED_ID_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjY3MTk2NzgzNTFhNWZhZWRjMmU3MDI3NGJiZWE2MmRhMmE4YzRhMTIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTA5NTk4MzI3ODM2NzMwNDE0MDMiLCJlbWFpbCI6ImdvcmFlMDJAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJtQkNPTTBpaFNlUU5QbGRpVmNHQ1lBIiwibmFtZSI6IuuVoey5oCIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NJUmh2cHRyZzFTOVNFU19MOVIzVExmN3FKclc2SVphYkdpZEdFLXRiQUtnQ2V6bGhvPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6IuuVoey5oCIsImlhdCI6MTcxNjk3MjQ2NCwiZXhwIjoxNzE2OTc2MDY0fQ.WROHoh9Uf18Wod4GRDZ_uNEK8V7DM7eigCcLfPqwKow_l7Rdx9gebw4o_QL94C3HfVFphLcMYKTTC7jLjCmsKSrR127wRvepXcWIUvysB8qtp9S3aG5DUe7ZPdR2sAE-vKY4jpoN6Wl8BGF1MqhHV06B6KmrjWrbPX8oOnTR03ASSTnlkdw6IO2_PE_Cl2SwN3bqmOLwtgasRFraL8uHcw6eBe4vcaoMl5-EnrmUqznrf9XH_rsKO16S_YOq--tn2hQs5Ed5OsRiQI5j_I0NQESjOHjoMADOeWABMkKBWDeqv64aCVk9ybVKY5NQOydkDbQ6dlHy2h6DwfF6NGBS5A";

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
