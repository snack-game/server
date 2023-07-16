package com.snackgame.server.config;

import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.snackgame.server.member.business.domain.Member;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

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
                        .addSecuritySchemes("jwtAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                )
                .addServersItem(new Server().url("/"));
    }

}
