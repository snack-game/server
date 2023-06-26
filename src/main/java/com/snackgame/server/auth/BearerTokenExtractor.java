package com.snackgame.server.auth;

import com.snackgame.server.auth.exception.TokenUnresolvableException;

public class BearerTokenExtractor {

    private static final String BEARER_TYPE = "Bearer";

    public String extract(String authorization) {
        if ((authorization.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
            return authorization.substring(BEARER_TYPE.length()).trim();
        }
        throw new TokenUnresolvableException();
    }
}
