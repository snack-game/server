package com.snackgame.server.auth.oauth;

import java.security.interfaces.RSAPublicKey;

import org.springframework.stereotype.Component;

import com.auth0.jwk.InvalidPublicKeyException;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.snackgame.server.auth.oauth.payload.IdTokenPayload;

@Component
public class IdTokenResolver {

    public void verify(final String rawIdToken) {
        try {
            DecodedJWT decoded = JWT.decode(rawIdToken);
            Provider provider = Provider.thatMatches(decoded.getIssuer());

            Jwk jwk = provider.getJwkProvider().get(decoded.getKeyId());
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey)jwk.getPublicKey(), null);

            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(rawIdToken);
        } catch (InvalidPublicKeyException e) {
            throw new IllegalArgumentException("IdTokenNotReadableException", e);
        } catch (JWTDecodeException e) {
            throw new IllegalArgumentException("InvalidIdTokenException", e);
        } catch (TokenExpiredException e) {
            throw new IllegalArgumentException("ExpireIdTokenException", e);
        } catch (JwkException e) {
            throw new IllegalArgumentException("JwkException", e);
        }
    }

    public IdTokenPayload resolve(final String rawIdToken) {
        DecodedJWT decoded = JWT.decode(rawIdToken);
        Provider provider = Provider.thatMatches(decoded.getIssuer());

        return provider.getPayloadConstructor().executeWith(decoded.getClaims());
    }
}
