package com.snackgame.server.auth.oauth.config;

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

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;

@Configuration
@EnableWebSecurity
@Profile({"local", "production"})
public class OAuthConfig {

    public OAuthConfig(Paths authCustomPaths) {
        addOAuthTo(authCustomPaths);
    }

    @Bean
    public SecurityFilterChain oAuth2FilterChain(HttpSecurity http) throws Exception {
        http.removeConfigurer(DefaultLoginPageConfigurer.class);
        return http
                .csrf().disable()
                .addFilterBefore(
                        new SessionOAuthRequestStoringFilter(),
                        OAuth2AuthorizationRequestRedirectFilter.class
                )
                .oauth2Login(configurer -> configurer
                        .successHandler(new OAuthSuccessHandler())
                        .failureHandler(new OAuthFailureHandler())
                )
                .build();
    }

    public void addOAuthTo(Paths paths) {
        paths
                .addPathItem("/oauth2/authorization/google", new PathItem()
                        .description("구글 OAuth")
                        .get(new Operation()
                                .addTagsItem("OAuth")
                                .description(descriptionFor("google"))
                        ))
                .addPathItem("/oauth2/authorization/kakao", new PathItem()
                        .description("카카오 OAuth")
                        .get(new Operation()
                                .addTagsItem("OAuth")
                                .description(descriptionFor("kakao"))
                        ));
    }

    private String descriptionFor(String provider) {
        return "브라우저를 [리다이렉션](/oauth2/authorization/" + provider + ")시켜야 한다.\n\n"
               + "OAuth 후에는 Referrer의 `/oauth/success`나 `/oauth/failure` 로 리다이렉트 되어 결과를 전한다.\n\n"
               + "ex) `snackga.me/game/apple-game` → oAuth Process → `snackga.me/game/apple-game/oauth/success`\n\n"
               + "이 때, 쿠키를 통해 세션 ID가 저장되며 \n\n"
               + "이후 관련 요청에 쿠키를 싣어 OAuth 결과를 활용할 수 있다.";
    }
}
