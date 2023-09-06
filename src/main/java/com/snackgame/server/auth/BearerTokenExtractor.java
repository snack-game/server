package com.snackgame.server.auth;

import java.util.Arrays;
import java.util.Objects;

import javax.servlet.http.Cookie;

import com.snackgame.server.auth.exception.TokenUnresolvableException;

public class BearerTokenExtractor {

    private static final String BEARER_TYPE = "Bearer";

    public String extract(String authorization) {
        requireNonNull(authorization);
        if ((authorization.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
            return authorization.substring(BEARER_TYPE.length()).trim();
        }
        throw new TokenUnresolvableException();
    }

    public String extract(Cookie[] cookies) {
        requireNonNull(cookies);
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("accessToken"))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(TokenUnresolvableException::new);
    }

    private void requireNonNull(Object object) {
        if (Objects.isNull(object)) {
            throw new TokenUnresolvableException();
        }
    }
}
