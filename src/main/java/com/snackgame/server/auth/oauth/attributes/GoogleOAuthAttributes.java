package com.snackgame.server.auth.oauth.attributes;

import java.util.Map;

public class GoogleOAuthAttributes implements OAuthAttributes {

    private final Map<String, Object> attributes;

    public GoogleOAuthAttributes(final Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getNickname() {
        return attributes.get("given_name").toString();
    }

    @Override
    public String getPictureUrl() {
        return attributes.get("picture").toString();
    }
}
