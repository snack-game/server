package com.snackgame.server.auth.oauth.oidc.jwk;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toMap;

import java.net.URL;
import java.security.PublicKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;

import com.snackgame.server.auth.exception.TokenExpiredException;
import com.snackgame.server.auth.exception.TokenInvalidException;
import com.snackgame.server.auth.oauth.oidc.payload.IdTokenPayload;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Identifiable;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.JwkSet;
import io.jsonwebtoken.security.Jwks;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwkProvider {

    private static final Duration VALID_DURATION = Duration.ofDays(7);

    private final URL keySetURL;
    private Map<String, PublicKey> keys;
    private LocalDateTime renewedAt;
    private final Function<Map<String, Object>, IdTokenPayload> payloadConstructor;

    public JwkProvider(URL keySetURL, Function<Map<String, Object>, IdTokenPayload> payloadConstructor) {
        this.keySetURL = keySetURL;
        this.payloadConstructor = payloadConstructor;
        renew();
    }

    private void renew() {
        JwkSet jwkSet = Jwks.setParser()
                .build()
                .parse(HttpStreamUtil.open(keySetURL));
        this.keys = jwkSet.getKeys().stream()
                .collect(toMap(Identifiable::getId, it -> (PublicKey)it.toKey()));
        this.renewedAt = now();
    }

    public boolean supports(IdToken idToken) {
        if (now().isAfter(renewedAt.plus(VALID_DURATION))) {
            renew();
        }
        return keys.containsKey(idToken.getKeyId());
    }

    public IdTokenPayload resolve(IdToken idToken) {
        PublicKey publicKey = getPublicKeyOf(idToken);
        JwtParser jwtParser = Jwts.parser().verifyWith(publicKey).build();
        try {
            return payloadConstructor.apply(
                    jwtParser.parseSignedClaims(idToken.getToken())
                            .getPayload()
            );
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("토큰 해석 중 오류가 있었습니다: {}", idToken.getToken());
            throw new TokenInvalidException();
        }
    }

    private PublicKey getPublicKeyOf(IdToken idToken) {
        PublicKey publicKey = keys.get(idToken.getKeyId());
        if (publicKey == null) {
            log.warn("토큰에 맞는 키를 찾을 수 없었습니다: {}", idToken.getKeyId());
            throw new TokenInvalidException();
        }
        return publicKey;
    }
}
