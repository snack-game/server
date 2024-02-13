package com.snackgame.server.auth.token.support;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.snackgame.server.auth.token.dto.TokensDto;
import com.snackgame.server.auth.token.util.JwtProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenToCookies {

    private static final String EMPTY = "";
    private static final String SAME_SITE_OPTION = "None";

    private final JwtProvider accessTokenProvider;
    private final JwtProvider refreshTokenProvider;

    public String[] from(TokensDto tokens) {
        return new String[] {
                baseCookieFrom(accessTokenProvider.getCanonicalName(), tokens.getAccessToken())
                        .path("/")
                        .build().toString(),
                baseCookieFrom(refreshTokenProvider.getCanonicalName(), tokens.getRefreshToken())
                        .path("/tokens/me")
                        .build()
                        .toString()
        };
    }

    public String[] empty() {
        return new String[] {
                baseCookieFrom(accessTokenProvider.getCanonicalName(), EMPTY)
                        .path("/")
                        .build().toString(),
                ResponseCookie.from(refreshTokenProvider.getCanonicalName(), EMPTY)
                        .path("/tokens/me")
                        .build().toString()
        };
    }

    private ResponseCookie.ResponseCookieBuilder baseCookieFrom(String name, String value) {
        return ResponseCookie.from(name, value)
                .maxAge(refreshTokenProvider.getExpiry())
                .httpOnly(true)
                .secure(true)
                .sameSite(SAME_SITE_OPTION);
    }
}
