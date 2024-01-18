package com.snackgame.server.auth.token.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.snackgame.server.common.domain.BaseEntity;

@Entity
public class RefreshToken extends BaseEntity {

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
