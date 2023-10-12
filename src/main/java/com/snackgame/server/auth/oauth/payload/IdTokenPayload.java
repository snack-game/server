package com.snackgame.server.auth.oauth.payload;

import com.snackgame.server.auth.oauth.Provider;

public interface IdTokenPayload {

    String getNickname();

    String getUsername();

    Provider getProvider();
}
