package com.snackgame.server.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.snackgame.server.auth.oauth.support.SocialMemberSavingArgumentResolver;
import com.snackgame.server.auth.token.support.JwtMemberArgumentResolver;
import com.snackgame.server.auth.token.support.TokensResolver;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String HOST_NAME = "snackga.me";

    private final TokensResolver tokensResolver;
    private final JwtMemberArgumentResolver jwtMemberArgumentResolver;
    private final SocialMemberSavingArgumentResolver socialMemberSavingArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(tokensResolver);
        resolvers.add(jwtMemberArgumentResolver);
        resolvers.add(socialMemberSavingArgumentResolver);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addStatusController("/", HttpStatus.OK);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .exposedHeaders("Location")
                .allowedOriginPatterns(
                        "https://" + HOST_NAME,
                        "https://api." + HOST_NAME,
                        "https://dev." + HOST_NAME,
                        "https://dev-api." + HOST_NAME,
                        "https://*snack-game.vercel.app",
                        "http://localhost:[*]"
                )
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
                .allowCredentials(true);
    }
}
