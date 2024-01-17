package com.snackgame.server.auth.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.snackgame.server.auth.jwt.util.JwtProvider;

@Configuration
public class JwtProviderConfig {

    @Bean
    public JwtProvider accessTokenProvider(
            @Value("${security.jwt.token.access-secret-key}") String secretKey,
            @Value("${security.jwt.token.access-expire-length}") long expireMilliseconds) {
        return new JwtProvider(secretKey, expireMilliseconds);
    }

    @Bean
    public JwtProvider refreshTokenProvider(
            @Value("${security.jwt.token.refresh-secret-key}") String secretKey,
            @Value("${security.jwt.token.refresh-expire-length}") long expireMilliseconds) {
        return new JwtProvider(secretKey, expireMilliseconds);
    }
}
