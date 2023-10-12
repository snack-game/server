package com.snackgame.server.auth.oauth.payload;

import java.util.Map;

import com.auth0.jwt.interfaces.Claim;
import com.snackgame.server.auth.oauth.Provider;

public class GoogleIdTokenPayload implements IdTokenPayload {

    private final Map<String, Claim> claims;

    public GoogleIdTokenPayload(final Map<String, Claim> claims) {
        this.claims = claims;
    }

    @Override
    public String getNickname() {
        return claims.get("name").asString();
    }

    @Override
    public String getUsername() {
        return claims.get("sub").asString();
    }

    @Override
    public Provider getProvider() {
        return Provider.GOOGLE;
    }
}
