package com.snackgame.server.config;

import java.util.Arrays;

import org.springdoc.core.SpringDocUtils;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.snackgame.server.auth.jwt.Authenticated;
import com.snackgame.server.auth.oauth.support.JustAuthenticated;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    private static final String JWT_SECURITY_KEY = "jwtAuth";
    private static final SecurityRequirement JWT_SECURITY_ITEM = new SecurityRequirement().addList(
            "jwtAuth"
    );

    static {
        SpringDocUtils.getConfig()
                .addAnnotationsToIgnore(Authenticated.class, JustAuthenticated.class);
    }

    @Bean
    public OpenAPI openApiSpecification() {
        return new OpenAPI()
                .info(new Info()
                        .version("v0.0.1")
                        .title("스낵게임 API")
                )
                .paths(oAuth2Paths())
                .components(new Components()
                        .addSecuritySchemes(JWT_SECURITY_KEY, jwtSecurityScheme())
                )
                .addServersItem(new Server().url("/"));
    }

    private Paths oAuth2Paths() {

        return new Paths()
                .addPathItem("/oauth2/authorization/google", new PathItem()
                        .description("구글 로그인")
                        .get(new Operation()
                                .addTagsItem("소셜 로그인")
                                .description(descriptionFor("google"))
                        ))
                .addPathItem("/oauth2/authorization/kakao", new PathItem()
                        .description("카카오 로그인")
                        .get(new Operation()
                                .addTagsItem("소셜 로그인")
                                .description(descriptionFor("kakao"))
                        ));
    }

    private String descriptionFor(String provider) {
        return "브라우저를 [직접 접근](/oauth2/authorization/" + provider + ")시켜야 한다.\n\n"
               + "소셜 로그인 후에는 처음 주소의 `/oauth/success`나 `/oauth/failure` 로 리다이렉트 된다.\n\n"
               + "ex) `snackga.me/game/apple-game` → oAuth Process → `snackga.me/game/apple-game/oauth/success`\n\n"
               + "이 때, Query String에 JSESSIONID가 포함되며, \n\n"
               + "이후 관련 소셜 로그인 요청에는 발급된 JSESSIONID 쿠키를 함께 싣어야 한다.";
    }

    private SecurityScheme jwtSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }

    @Bean
    public OperationCustomizer authOperationMarker() {
        return (operation, handlerMethod) -> {
            Arrays.stream(handlerMethod.getMethodParameters())
                    .filter(it -> it.hasParameterAnnotation(Authenticated.class))
                    .findAny()
                    .ifPresent(it -> operation.addSecurityItem(JWT_SECURITY_ITEM));
            return operation;
        };
    }
}
