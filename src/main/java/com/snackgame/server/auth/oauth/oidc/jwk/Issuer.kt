package com.snackgame.server.auth.oauth.oidc.jwk

import com.snackgame.server.auth.oauth.oidc.payload.AppleIdTokenPayload
import com.snackgame.server.auth.oauth.oidc.payload.GoogleIdTokenPayload
import com.snackgame.server.auth.oauth.oidc.payload.IdTokenPayload
import com.snackgame.server.auth.oauth.oidc.payload.KakaoIdTokenPayload
import java.net.URL
import java.util.*

enum class Issuer(
    private val jwkProvider: JwkProvider
) {

    KAKAO(JwkProvider(URL("https://kauth.kakao.com/.well-known/jwks.json"), ::KakaoIdTokenPayload)),
    APPLE(JwkProvider(URL("https://appleid.apple.com/auth/keys"), ::AppleIdTokenPayload)),
    GOOGLE(JwkProvider(URL("https://www.googleapis.com/oauth2/v3/certs"), ::GoogleIdTokenPayload));

    fun resolve(idToken: IdToken): IdTokenPayload {
        return jwkProvider.resolve(idToken)
    }

    companion object {
        @JvmStatic
        fun of(idToken: IdToken): Optional<Issuer> {
            return entries.stream()
                .filter { it.jwkProvider.supports(idToken) }
                .findFirst()
        }
    }
}
