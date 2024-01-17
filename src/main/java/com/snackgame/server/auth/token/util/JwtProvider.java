package com.snackgame.server.auth.token.util;

import java.util.Date;
import java.util.Objects;

import com.snackgame.server.auth.exception.TokenExpiredException;
import com.snackgame.server.auth.exception.TokenInvalidException;
import com.snackgame.server.auth.exception.TokenUnresolvableException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtProvider {

    private final String secretKey;
    private final long expireMilliseconds;

    public JwtProvider(
            String secretKey,
            long expireMilliseconds
    ) {
        this.secretKey = secretKey;
        this.expireMilliseconds = expireMilliseconds;
    }

    public String createTokenWith(Long memberId) {
        Claims claims = Jwts.claims().setSubject(memberId.toString());
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expireMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createTokenWith(String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expireMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getSubjectFrom(String token) {
        JwtParser jwtParser = Jwts.parser().setSigningKey(secretKey);
        return jwtParser.parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public void validate(String token) {
        try {
            validateHas(token);
            String subject = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                    .getBody()
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
}

