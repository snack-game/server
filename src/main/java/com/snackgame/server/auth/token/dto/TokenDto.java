package com.snackgame.server.auth.token.dto;

import java.time.Duration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TokenDto {

    private final String accessToken;
    private final String refreshToken;
    private final Duration accessTokenExpiry;
    private final Duration refreshTokenExpiry;
}
