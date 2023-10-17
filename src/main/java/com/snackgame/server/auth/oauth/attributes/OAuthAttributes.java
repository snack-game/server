package com.snackgame.server.auth.oauth.attributes;

public interface OAuthAttributes {

    String getProvider();

    String getId();

    String getEmail();

    String getName();

    String getNickname();

    String getPictureUrl();
}
