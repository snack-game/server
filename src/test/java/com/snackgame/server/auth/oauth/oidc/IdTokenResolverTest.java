package com.snackgame.server.auth.oauth.oidc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.snackgame.server.auth.oauth.oidc.payload.IdTokenPayload;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class IdTokenResolverTest {

    private static final String EXPIRED_ID_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjY3MTk2NzgzNTFhNWZhZWRjMmU3MDI3NGJiZWE2MmRhMmE4YzRhMTIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTA5NTk4MzI3ODM2NzMwNDE0MDMiLCJlbWFpbCI6ImdvcmFlMDJAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJtQkNPTTBpaFNlUU5QbGRpVmNHQ1lBIiwibmFtZSI6IuuVoey5oCIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NJUmh2cHRyZzFTOVNFU19MOVIzVExmN3FKclc2SVphYkdpZEdFLXRiQUtnQ2V6bGhvPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6IuuVoey5oCIsImlhdCI6MTcxNjk3MjQ2NCwiZXhwIjoxNzE2OTc2MDY0fQ.WROHoh9Uf18Wod4GRDZ_uNEK8V7DM7eigCcLfPqwKow_l7Rdx9gebw4o_QL94C3HfVFphLcMYKTTC7jLjCmsKSrR127wRvepXcWIUvysB8qtp9S3aG5DUe7ZPdR2sAE-vKY4jpoN6Wl8BGF1MqhHV06B6KmrjWrbPX8oOnTR03ASSTnlkdw6IO2_PE_Cl2SwN3bqmOLwtgasRFraL8uHcw6eBe4vcaoMl5-EnrmUqznrf9XH_rsKO16S_YOq--tn2hQs5Ed5OsRiQI5j_I0NQESjOHjoMADOeWABMkKBWDeqv64aCVk9ybVKY5NQOydkDbQ6dlHy2h6DwfF6NGBS5A";

    @Test
    void 구글_ID_토큰을_해석한다() {
        IdTokenResolver idTokenResolver = new IdTokenResolver();
        IdTokenPayload payload = idTokenResolver.resolve(EXPIRED_ID_TOKEN);
        assertThat(payload.getId()).isEqualTo("110959832783673041403");
        assertThat(payload.getEmail()).isEqualTo("gorae02@gmail.com");
        assertThat(payload.getProvider()).isEqualTo("google");
        assertThat(payload.getName()).isEqualTo("땡칠");
        assertThat(payload.getPicture()).isEqualTo(
                "https://lh3.googleusercontent.com/a/ACg8ocIRhvptrg1S9SES_L9R3TLf7qJrW6IZabGidGE-tbAKgCezlho=s96-c");
    }
}
