package com.snackgame.server.auth.token.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.snackgame.server.common.domain.BaseEntity;

@Entity
@SQLDelete(sql = "UPDATE refresh_token SET deleted = true WHERE refresh_token_id = ?")
@Where(clause = "deleted = false")
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id", updatable = false)
    private Long id;
    private String token;

    private boolean deleted = Boolean.FALSE;

    public RefreshToken(String token) {
        this.token = token;
    }

    public RefreshToken() {
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
