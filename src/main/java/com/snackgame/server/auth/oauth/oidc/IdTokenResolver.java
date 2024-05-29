package com.snackgame.server.auth.oauth.oidc;

import org.springframework.stereotype.Component;

import com.snackgame.server.auth.exception.TokenInvalidException;
import com.snackgame.server.auth.oauth.oidc.jwk.IdToken;
import com.snackgame.server.auth.oauth.oidc.jwk.Issuer;
import com.snackgame.server.auth.oauth.oidc.payload.IdTokenPayload;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class IdTokenResolver {

    public IdTokenPayload resolve(String rawIdToken) {
        IdToken idToken = new IdToken(rawIdToken);
        Issuer issuer = Issuer.of(idToken)
                .orElseThrow(() -> {
                    log.warn("지원하지 않는 IdToken이 있었습니다: {}", rawIdToken);
                    return new TokenInvalidException();
                });
        return issuer.resolve(idToken);
    }
}
