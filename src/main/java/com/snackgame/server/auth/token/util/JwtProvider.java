package com.snackgame.server.auth.token.util;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

import javax.crypto.SecretKey;

import com.snackgame.server.auth.exception.TokenExpiredException;
import com.snackgame.server.auth.exception.TokenInvalidException;
import com.snackgame.server.auth.exception.TokenUnresolvableException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtProvider {

    private final String canonicalName;
    private final SecretKey key;
    private final Duration expiry;

    public JwtProvider(
            String canonicalName,
            String secretKey,
            Duration expiry
    ) {
        this.canonicalName = canonicalName;
        this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKey.getBytes()));
        this.expiry = expiry;
    }

    public String createTokenWith(String payload) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expiry.toMillis());

        return Jwts.builder()
                .subject(payload)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public String getSubjectFrom(String token) {
        JwtParser jwtParser = Jwts.parser().verifyWith(key).build();

        return jwtParser.parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public void validate(String token) {
        try {
            validateHas(token);
            String subject = Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            validateHas(subject);
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        } catch (JwtException | IllegalArgumentException e) {
            throw new TokenInvalidException();
        }
    }

    private void validateHas(String token) {
        if (Objects.isNull(token)) {
            throw new TokenUnresolvableException();
        }
    }

    public Duration getExpiry() {
        return expiry;
    }

    public String getCanonicalName() {
        return canonicalName;
    }
}

