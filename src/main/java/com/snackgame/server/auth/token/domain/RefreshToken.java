package com.snackgame.server.auth.token.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.snackgame.server.common.domain.BaseEntity;

@Entity
@SQLDelete(sql = "UPDATE refresh_token SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private final boolean deleted = false;

    protected RefreshToken() {
    }

    public RefreshToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
