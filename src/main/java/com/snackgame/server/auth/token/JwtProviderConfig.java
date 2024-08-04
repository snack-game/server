package com.snackgame.server.auth.token;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.snackgame.server.auth.token.util.JwtProvider;

@Configuration
public class JwtProviderConfig {

    @Bean
    public JwtProvider accessTokenProvider(
            @Value("${security.jwt.token.access-secret-key}") String secretKey,
            @Value("${security.jwt.token.access-expiry-days}") long expiryInDays) {
        return new JwtProvider("accessToken", secretKey, Duration.ofSeconds(20));
    }

    @Bean
    public JwtProvider refreshTokenProvider(
            @Value("${security.jwt.token.refresh-secret-key}") String secretKey,
            @Value("${security.jwt.token.refresh-expiry-days}") long expiryInDays) {
        return new JwtProvider("refreshToken", secretKey, Duration.ofSeconds(60));
    }
}
