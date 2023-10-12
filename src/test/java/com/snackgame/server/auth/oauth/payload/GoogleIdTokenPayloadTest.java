package com.snackgame.server.auth.oauth.payload;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.snackgame.server.auth.oauth.Provider;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GoogleIdTokenPayloadTest {

    private static final String EXPIRED_ID_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjkxMWUzOWUyNzkyOGFlOWYxZTlkMWUyMTY0NmRlOTJkMTkzNTFiNDQiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIxMjIwMzcyNTY1NjAtNm45b20wZm1ibTlkZ2FmN3RtdGFpamZhbmVkdjFybTEuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIxMjIwMzcyNTY1NjAtMWo1dTN2YnQ0NHRtMHJtaGI5cWo0bWg4bGFsbzNlMXIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDQxMDYwMDM2NjEyMjg5MjAzNzEiLCJlbWFpbCI6ImtpbWppbnVrMTk5OUBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6Iuq5gOynhOyasSIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQWNIVHRkeUxlcXFheE9wS3l1QUZmNnpqbUJNX0xuRGJkNkJTMTNmSmtGczdQWHg9czk2LWMiLCJnaXZlbl9uYW1lIjoi7KeE7JqxIiwiZmFtaWx5X25hbWUiOiLquYAiLCJsb2NhbGUiOiJrbyIsImlhdCI6MTY5MTQ3NTcxMCwiZXhwIjoxNjkxNDc5MzEwfQ.O41Jfdb_Y0swfZ2r1oDG9NDO73dYYmPStzh8l90BEbwPoPQPeySBmRO9UnCnFaL_B6p18vKXfpgxOn6RDBwknHB6dRrRqElKFhEaCTp63TMMc2g-EijdsKDbhnUQWsTyuWqVflnNsCk0HQPU9_MevCuPIH-_gDdIHV6SQwVuZDWMjlzAdZXZCyyH09q22QMCsB1-zaPZHKHLShtGspRN5HUVvINhshB6Lip1OOe3cUFD-g41o3pFAOpDFbMMif1WVK5Lv6031SQL4GbhGN3Uum62-wgiU268uLwHYRFLNqkjR1guMNK4ieBmt46SkG1VOpfzcPE6I4p7x1lhUdN9Pg";

    @Test
    void GOOGLE_ID_TOKEN_CLAIM을_해석한다() {
        DecodedJWT decoded = JWT.decode(EXPIRED_ID_TOKEN);
        IdTokenPayload payload = new GoogleIdTokenPayload(decoded.getClaims());

        assertThat(payload.getProvider()).isEqualTo(Provider.GOOGLE);
        assertThat(payload.getUsername()).isEqualTo("104106003661228920371");
        assertThat(payload.getNickname()).isEqualTo("김진욱");
    }
}
