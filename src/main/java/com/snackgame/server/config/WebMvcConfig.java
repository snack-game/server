package com.snackgame.server.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.snackgame.server.auth.jwt.BearerTokenExtractor;
import com.snackgame.server.auth.jwt.JwtMemberArgumentResolver;
import com.snackgame.server.auth.jwt.JwtProvider;
import com.snackgame.server.auth.oauth.support.SocialMemberSavingArgumentResolver;
import com.snackgame.server.member.business.domain.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String HOST_NAME = "snackga.me";

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new JwtMemberArgumentResolver(
                new BearerTokenExtractor(),
                jwtProvider,
                memberRepository
        ));
        resolvers.add(new SocialMemberSavingArgumentResolver(memberRepository));
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "https://" + HOST_NAME);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .exposedHeaders("Location")
                .allowedOriginPatterns(
                        "https://" + HOST_NAME,
                        "https://api." + HOST_NAME,
                        "https://*snack-game.vercel.app",
                        "http://localhost:[*]"
                )
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH");
    }
}
