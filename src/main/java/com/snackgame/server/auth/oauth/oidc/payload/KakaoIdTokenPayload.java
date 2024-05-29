package com.snackgame.server.auth.oauth.oidc.payload;

import java.util.Map;

public class KakaoIdTokenPayload implements IdTokenPayload {

    private final Map<String, Object> payload;

    public KakaoIdTokenPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getId() {
        return payload.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return payload.get("email").toString();
    }

    @Override
    public String getName() {
        return payload.get("name").toString();
    }

    @Override
    public String getPicture() {
        return (String)payload.getOrDefault("picture", null);
    }
}
