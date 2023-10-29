package com.snackgame.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

import com.snackgame.server.auth.oauth.OAuthFailureHandler;
import com.snackgame.server.auth.oauth.OAuthSuccessHandler;
import com.snackgame.server.auth.oauth.SessionOAuthRequestStoringFilter;

@Configuration
@EnableWebSecurity
@Profile({"local", "production"})
public class OAuth2ClientConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.removeConfigurer(DefaultLoginPageConfigurer.class);
        return http
                .csrf().disable()
                .oauth2Login(configurer -> configurer
                        .successHandler(new OAuthSuccessHandler())
                        .failureHandler(new OAuthFailureHandler())
                )
                .addFilterBefore(
                        new SessionOAuthRequestStoringFilter(),
                        OAuth2AuthorizationRequestRedirectFilter.class
                )
                .build();
    }
}
