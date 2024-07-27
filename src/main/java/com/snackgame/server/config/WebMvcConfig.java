package com.snackgame.server.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String HOST_NAME = "snackga.me";

    private final List<HandlerMethodArgumentResolver> argumentResolvers;

    public WebMvcConfig(List<HandlerMethodArgumentResolver> argumentResolvers) {
        this.argumentResolvers = argumentResolvers;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(argumentResolvers);
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
                        "https://dev." + HOST_NAME,
                        "https://dev-api." + HOST_NAME,
                        "https://*snack-game.vercel.app",
                        "http://localhost:[*]"
                )
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
                .allowCredentials(true);
    }
}
