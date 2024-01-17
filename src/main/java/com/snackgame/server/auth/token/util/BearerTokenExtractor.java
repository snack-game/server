package com.snackgame.server.auth.token.util;

import java.util.Objects;

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

    private void requireNonNull(String authorization) {
        if (Objects.isNull(authorization)) {
            throw new TokenUnresolvableException();
        }
    }
}
