package com.snackgame.server.config;

import java.util.Arrays;
import java.util.List;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SpringDocUtils;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.snackgame.server.auth.oauth.support.JustAuthenticated;
import com.snackgame.server.auth.token.support.Authenticated;
import com.snackgame.server.auth.token.support.TokensFromCookie;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    private static final String ACCESS_TOKEN_SECURITY_KEY = "accessToken";
    private static final String REFRESH_TOKEN_SECURITY_KEY = "refreshToken";

    static {
        SpringDocUtils.getConfig()
                .addAnnotationsToIgnore(Authenticated.class, JustAuthenticated.class, TokensFromCookie.class);
    }

    @Bean
    public OpenAPI openApiSpecification() {
        return new OpenAPI()
                .info(new Info()
                        .version("v1.0")
                        .title("스낵게임 API")
                )
                .components(new Components()
                        .addSecuritySchemes(ACCESS_TOKEN_SECURITY_KEY, accessTokenSecurityScheme())
                        .addSecuritySchemes(REFRESH_TOKEN_SECURITY_KEY, refreshTokenSecurityScheme())
                )
                .addServersItem(new Server().url("/"));
    }

    @Order(1)
    @Bean
    public GroupedOpenApi userApis() {
        return GroupedOpenApi.builder()
                .group("1-user")
                .displayName("일반 사용자 API")
                .pathsToExclude("/tokens/**")
                .build()
                .addAllOperationCustomizer(List.of(accessTokenOperationMarker(), refreshTokenOperationMarker()));
    }

    @Order(2)
    @Bean
    public GroupedOpenApi authApis() {
        return GroupedOpenApi.builder()
                .group("2-auth")
                .displayName("인증 API")
                .pathsToMatch("/tokens/**")
                .addOpenApiCustomiser(openApi -> {
                    authCustomPaths().putAll(openApi.getPaths());
                    openApi.paths(authCustomPaths());
                })
                .build()
                .addAllOperationCustomizer(List.of(accessTokenOperationMarker(), refreshTokenOperationMarker()));
    }

    @Bean
    public Paths authCustomPaths() {
        return new Paths();
    }

    private SecurityScheme accessTokenSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.COOKIE)
                .name("accessToken");
    }

    private SecurityScheme refreshTokenSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.COOKIE)
                .name("refreshToken");
    }

    public OperationCustomizer accessTokenOperationMarker() {
        return (operation, handlerMethod) -> {
            Arrays.stream(handlerMethod.getMethodParameters())
                    .filter(it -> it.hasParameterAnnotation(Authenticated.class))
                    .findAny()
                    .ifPresent(it -> operation.addSecurityItem(new SecurityRequirement()
                            .addList(ACCESS_TOKEN_SECURITY_KEY)
                    ));
            return operation;
        };
    }

    public OperationCustomizer refreshTokenOperationMarker() {
        return (operation, handlerMethod) -> {
            Arrays.stream(handlerMethod.getMethodParameters())
                    .filter(it -> it.hasParameterAnnotation(TokensFromCookie.class))
                    .findAny()
                    .ifPresent(it -> operation.addSecurityItem(new SecurityRequirement()
                            .addList(ACCESS_TOKEN_SECURITY_KEY)
                            .addList(REFRESH_TOKEN_SECURITY_KEY)
                    ));
            return operation;
        };
    }
}
