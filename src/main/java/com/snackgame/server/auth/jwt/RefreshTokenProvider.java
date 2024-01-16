package com.snackgame.server.auth.jwt;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.snackgame.server.auth.exception.InvalidTokenException;
import com.snackgame.server.auth.exception.TokenUnresolvableException;
import com.snackgame.server.auth.jwt.domain.RefreshToken;
import com.snackgame.server.auth.jwt.repository.RefreshTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class RefreshTokenProvider {

    private static final String COOKIE_PATH = "/";

    private static final String COOKIE_DOMAIN = "snackga.me";
    private final String refreshSecretKey;
    private final long refreshExpireMilliseconds;

    private final RefreshTokenRepository tokenRepository;
    private final AccessTokenProvider accessTokenProvider;

    public RefreshTokenProvider(
            @Value("${security.jwt.token.refresh-secret-key}") String refreshSecretKey,
            @Value("${security.jwt.token.refresh-expire-length}") long refreshExpireMilliseconds,
            RefreshTokenRepository tokenRepository, AccessTokenProvider accessTokenProvider) {
        this.refreshSecretKey = refreshSecretKey;
        this.refreshExpireMilliseconds = refreshExpireMilliseconds;
        this.tokenRepository = tokenRepository;
        this.accessTokenProvider = accessTokenProvider;
    }

    public String issue(Long memberId) {
        String refreshToken = createTokenWith(memberId.toString());
        tokenRepository.save(new RefreshToken(refreshToken));
        return refreshToken;
    }

    public List<String> reissue(String previous) {
        validate(previous);

        String subject = getSubjectFrom(previous);
        String accessToken = accessTokenProvider.createTokenWith(subject);
        String refreshToken = createTokenWith(subject);

        tokenRepository.save(new RefreshToken(refreshToken));
        tokenRepository.deleteByToken(previous);

        return List.of(accessToken, refreshToken);
    }

    private String createTokenWith(String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshExpireMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
                .compact();
    }

    /**
     * RefreshToken 쿠키에 저장
     *
     * @param response
     * @param refreshToken
     */
    public void sendRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(HttpHeaders.AUTHORIZATION, refreshToken);
        cookie.setMaxAge((int)refreshExpireMilliseconds);
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    public String getSubjectFrom(String token) {
        JwtParser jwtParser = Jwts.parser().setSigningKey(refreshSecretKey);
        return jwtParser.parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public void validate(String token) {
        try {
            validateHas(token);
            String subject = Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            validateHas(subject);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
    }

    private void validateHas(String token) {
        if (Objects.isNull(token)) {
            throw new TokenUnresolvableException();
        }
    }
}
