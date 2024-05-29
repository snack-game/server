package com.snackgame.server.auth.oauth.oidc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.snackgame.server.auth.oauth.oidc.payload.IdTokenPayload;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class IdTokenResolverTest {

    private static final String EXPIRED_ID_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjY3MTk2NzgzNTFhNWZhZWRjMmU3MDI3NGJiZWE2MmRhMmE4YzRhMTIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTA5NTk4MzI3ODM2NzMwNDE0MDMiLCJlbWFpbCI6ImdvcmFlMDJAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJWbmVzOWZtTGxCV3BsZ2p5NG81cDdRIiwiaWF0IjoxNzE2OTcwMjE4LCJleHAiOjE3MTY5NzM4MTh9.IyWIGRprPTO7u_LwpsPPxGQ6_yNAjY9XzysBoR9vifuD0j9-zuhdGQKNUvLjiNVKoG1cW0FPag1iR-iSWhJHx8jIfoGZ2yr6gK2PUcn15B8MTF7PxVAquRrDhPxvpsl_QZnHVAcF33sOPeYMr_aQKLGVYMoHVSOLizAPC5TNBipePPjKjlErnfYOEiHGzIix_o2E7AgyKoJY71Mnc73bgm0VvA7mnznm-tIHn5An1-YC6lC6JvO4_vgGlh3ezmw0902oYx5iw3taACl3F6k_IxLJGStowyzCvR1eS7ZcLFWU6goHBv5j7117WJHimNaRnfyvtYJybqHE32xMYhGT-Q";

    @Test
    void 구글_ID_토큰을_해석한다() {
        IdTokenResolver idTokenResolver = new IdTokenResolver();
        IdTokenPayload payload = idTokenResolver.resolve(EXPIRED_ID_TOKEN);
        assertThat(payload.getId()).isEqualTo("110959832783673041403");
        assertThat(payload.getEmail()).isEqualTo("gorae02@gmail.com");
        assertThat(payload.getProvider()).isEqualTo("google");
    }
}
