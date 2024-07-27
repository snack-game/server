package com.snackgame.server.auth.token.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokensDto {

    private final String accessToken;
    private final String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
