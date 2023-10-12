package com.snackgame.server.auth.oauth;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.snackgame.server.auth.oauth.payload.GoogleIdTokenPayload;
import com.snackgame.server.auth.oauth.payload.KakaoIdTokenPayload;
import com.snackgame.server.auth.oauth.payload.PayloadConstructor;

public enum Provider {

    GOOGLE("https://accounts.google.com", URI.create("https://www.googleapis.com/oauth2/v3/certs"),
            GoogleIdTokenPayload::new),
    KAKAO("https://kauth.kakao.com", URI.create("https://kauth.kakao.com/.well-known/jwks.json"),
            KakaoIdTokenPayload::new);

    private final String issuer;
    private final JwkProvider jwkProvider;
    private final PayloadConstructor payloadConstructor;

    Provider(String issuer, URI publicKeyUri, PayloadConstructor payloadConstructor) {
        this.issuer = issuer;
        this.jwkProvider = buildJwkProviderFrom(publicKeyUri);
        this.payloadConstructor = payloadConstructor;
    }

    public static Provider thatMatches(String issuer) {
        return Arrays.stream(values())
                .filter(it -> it.issuer.equals(issuer))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 발급자입니다: " + issuer));
    }

    public JwkProvider getJwkProvider() {
        return jwkProvider;
    }

    public PayloadConstructor getPayloadConstructor() {
        return payloadConstructor;
    }

    private static JwkProvider buildJwkProviderFrom(URI uri) {
        try {
            return new JwkProviderBuilder(uri.toURL())
                    .cached(10, 7, TimeUnit.DAYS)
                    .build();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("OAuthResolverException.InvalidJwkUrlException()");
        }
    }
}
