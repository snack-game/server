package com.snackgame.server.config;

import java.util.Arrays;

import org.springdoc.core.SpringDocUtils;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.snackgame.server.member.business.domain.Member;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
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
                .addRequestWrapperToIgnore(Member.class);
    }

    @Bean
    public OpenAPI openApiSpecification() {
        return new OpenAPI()
                .info(new Info()
                        .version("v0.0.1")
                        .title("스낵게임 API")
                        .description(
                                "[더 자세한 설명](https://jumbled-droplet-70f.notion.site/API-30855489790c45e58d69adc1c7198b43)")
                )
                .components(new Components()
                        .addSecuritySchemes(JWT_SECURITY_KEY, jwtSecurityScheme())
                )
                .addServersItem(new Server().url("/"));
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
                    .filter(it -> Member.class.isAssignableFrom(it.getParameterType())
                                  && !it.hasParameterAnnotations())
                    .findAny()
                    .ifPresent(it -> operation.addSecurityItem(JWT_SECURITY_ITEM));
            return operation;
        };
    }
}
