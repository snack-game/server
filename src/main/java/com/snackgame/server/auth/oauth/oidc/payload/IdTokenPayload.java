package com.snackgame.server.auth.oauth.oidc.payload;

public interface IdTokenPayload {

    String getProvider();

    String getId();

    String getEmail();

    String getName();

    String getPicture();
}
