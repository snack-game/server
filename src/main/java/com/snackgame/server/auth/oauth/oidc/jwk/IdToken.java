package com.snackgame.server.auth.oauth.oidc.jwk;

import java.util.Base64;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.snackgame.server.auth.exception.TokenInvalidException;

public class IdToken {

    private final String idToken;

    public IdToken(String idToken) {
        validateHasThreeParts(idToken);
        this.idToken = idToken;
    }

    private void validateHasThreeParts(String idToken) {
        if (idToken != null && idToken.split("\\.").length != 3) {
            throw new TokenInvalidException();
        }
    }

    public String getIss() {
        String payload = idToken.split("\\.")[1];

        String decoded = new String(Base64.getDecoder().decode(payload));
        JsonElement jsonElement = JsonParser.parseString(decoded);
        return jsonElement.getAsJsonObject().get("iss").getAsString();
    }

    public String getAudience() {
        String payload = idToken.split("\\.")[1];

        String decoded = new String(Base64.getDecoder().decode(payload));
        JsonElement jsonElement = JsonParser.parseString(decoded);
        return jsonElement.getAsJsonObject().get("aud").getAsString();
    }

    public String getKeyId() {
        String header = idToken.split("\\.")[0];

        String decoded = new String(Base64.getDecoder().decode(header));
        JsonElement jsonElement = JsonParser.parseString(decoded);
        return jsonElement.getAsJsonObject().get("kid").getAsString();
    }

    public String getToken() {
        return idToken;
    }
}
