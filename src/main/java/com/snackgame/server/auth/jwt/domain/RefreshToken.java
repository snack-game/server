package com.snackgame.server.auth.jwt.domain;

public class RefreshToken {

    private String token;

    public RefreshToken(String token) {
        this.token = token;
    }

    public RefreshToken() {
    }

}
