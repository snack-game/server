package com.snackgame.server.auth.oauth.payload;

import java.util.Map;

import com.auth0.jwt.interfaces.Claim;

public interface PayloadConstructor {

    IdTokenPayload executeWith(Map<String, Claim> claims);
}
