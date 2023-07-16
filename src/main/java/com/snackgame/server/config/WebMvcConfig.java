package com.snackgame.server.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.snackgame.server.auth.BearerTokenExtractor;
import com.snackgame.server.auth.JwtMemberArgumentResolver;
import com.snackgame.server.auth.JwtProvider;
import com.snackgame.server.member.business.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new JwtMemberArgumentResolver(
                new BearerTokenExtractor(),
                jwtProvider,
                memberService
        ));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .exposedHeaders("Location")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH");
    }
}
