package com.snackgame.server.auth.jwt.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;

    public RefreshToken(String token) {
        this.token = token;
    }

    public RefreshToken() {
    }

    public String getToken() {
        return token;
    }
}
