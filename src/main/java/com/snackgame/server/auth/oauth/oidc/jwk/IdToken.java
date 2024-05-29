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

        return extractValueFrom(payload, "iss");
    }

    public String getAudience() {
        String payload = idToken.split("\\.")[1];

        return extractValueFrom(payload, "aud");
    }

    public String getKeyId() {
        String header = idToken.split("\\.")[0];

        return extractValueFrom(header, "kid");
    }

    private String extractValueFrom(String encodedJson, String key) {
        try {
            String decoded = new String(Base64.getDecoder().decode(encodedJson));
            JsonElement jsonElement = JsonParser.parseString(decoded);
            return jsonElement.getAsJsonObject().get(key).getAsString();
        } catch (IllegalStateException e) {
            throw new TokenInvalidException();
        }
    }

    public String getToken() {
        return idToken;
    }
}
