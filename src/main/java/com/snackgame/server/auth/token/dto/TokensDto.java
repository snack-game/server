package com.snackgame.server.auth.token.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TokensDto {

    private final String accessToken;
    private final String refreshToken;
}
