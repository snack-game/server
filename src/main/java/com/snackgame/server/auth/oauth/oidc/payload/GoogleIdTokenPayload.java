package com.snackgame.server.auth.oauth.oidc.payload;

import java.util.Map;

public class GoogleIdTokenPayload implements IdTokenPayload {

    private final Map<String, Object> payload;

    public GoogleIdTokenPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getId() {
        return payload.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return payload.get("email").toString();
    }
}
