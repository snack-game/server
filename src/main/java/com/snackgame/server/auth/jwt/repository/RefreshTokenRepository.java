package com.snackgame.server.auth.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snackgame.server.auth.jwt.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteByToken(String token);
}
