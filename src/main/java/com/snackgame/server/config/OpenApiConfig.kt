package com.snackgame.server.config

import com.snackgame.server.auth.oauth.support.JustAuthenticated
import com.snackgame.server.auth.token.support.Authenticated
import com.snackgame.server.auth.token.support.OptionalGuest
import com.snackgame.server.auth.token.support.TokensFromCookie
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.GroupedOpenApi
import org.springdoc.core.SpringDocUtils
import org.springdoc.core.customizers.OperationCustomizer
import org.springdoc.core.providers.JavadocProvider
import org.springdoc.openapi.javadoc.SpringDocJavadocProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.method.HandlerMethod
import javax.annotation.PostConstruct

@Configuration
class OpenApiConfig {

    @Bean
    fun openApiSpecification(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .version("v1.0")
                    .title("스낵게임 API")
            )
            .components(
                Components()
                    .addSecuritySchemes(ACCESS_TOKEN_SECURITY_SCHEME.name, ACCESS_TOKEN_SECURITY_SCHEME)
                    .addSecuritySchemes(REFRESH_TOKEN_SECURITY_SCHEME.name, REFRESH_TOKEN_SECURITY_SCHEME)
            )
            .addServersItem(Server().url("/"))
    }

    @Bean
    fun userApis(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("1-general")
            .displayName("일반")
            .pathsToExclude("/tokens/**", "/games/**")
            .build()
    }

    @Bean
    fun gameApis(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("2-game")
            .displayName("게임")
            .pathsToMatch("/games/**")
            .build()
    }

    @Bean
    fun authApis(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("3-auth")
            .displayName("인증")
            .pathsToMatch("/tokens/**")
            .addOpenApiCustomiser { openApi: OpenAPI ->
                authCustomPaths().putAll(openApi.paths)
                openApi.paths(authCustomPaths())
            }
            .build()
    }

    @Bean
    fun authCustomPaths(): Paths {
        return Paths()
    }

    @Bean
    fun deprecatedApis(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("98-deprecated")
            .displayName("Deprecated")
            .addOpenApiMethodFilter { method ->
                method.declaringClass.annotations.any { it is Deprecated || it is java.lang.Deprecated }
                        || method.annotations.any { it is Deprecated || it is java.lang.Deprecated }
            }
            .build()
    }

    companion object {
        val ACCESS_TOKEN_SECURITY_SCHEME: SecurityScheme = SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .`in`(SecurityScheme.In.COOKIE)
            .name("accessToken")
        val REFRESH_TOKEN_SECURITY_SCHEME: SecurityScheme = SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .`in`(SecurityScheme.In.COOKIE)
            .name("refreshToken")

        init {
            SpringDocUtils.getConfig()
                .addAnnotationsToIgnore(
                    Authenticated::class.java,
                    JustAuthenticated::class.java,
                    TokensFromCookie::class.java,
                    OptionalGuest::class.java
                )
        }
    }
}

@Configuration
class OpenApiCustomizationConfig(
    private val apiGroups: List<GroupedOpenApi>
) {

    @PostConstruct
    fun allScopeCustomizer() {
        apiGroups.forEach { apiGroup ->
            apiGroup.operationCustomizers += arrayOf(accessTokenOperationMarker, refreshTokenOperationMarker)
        }
        apiGroups.filterNot { it.displayName == "Deprecated" }
            .forEach { apiGroup ->
                apiGroup.openApiMethodFilters.add { method ->
                    method.declaringClass.annotations.none { it is Deprecated || it is java.lang.Deprecated }
                            && method.annotations.none { it is Deprecated || it is java.lang.Deprecated }
                }
            }
    }

    @Primary
    @Bean
    fun customizedJavadocProvider(): JavadocProvider {
        return object : SpringDocJavadocProvider() {
            override fun getFirstSentence(text: String?): String? {
                return text?.lines()?.firstOrNull()
            }
        }
    }

    private val accessTokenOperationMarker =
        OperationCustomizer { operation: Operation, handlerMethod: HandlerMethod ->
            val hasAnnotation = handlerMethod.methodParameters
                .any { it.hasParameterAnnotation(Authenticated::class.java) }
            if (hasAnnotation) {
                operation.addSecurityItem(
                    SecurityRequirement()
                        .addList(OpenApiConfig.ACCESS_TOKEN_SECURITY_SCHEME.name)
                )
            }
            operation
        }

    private val refreshTokenOperationMarker =
        OperationCustomizer { operation: Operation, handlerMethod: HandlerMethod ->
            val hasAnnotation = handlerMethod.methodParameters
                .any { it.hasParameterAnnotation(TokensFromCookie::class.java) }
            if (hasAnnotation) {
                operation.addSecurityItem(
                    SecurityRequirement()
                        .addList(OpenApiConfig.ACCESS_TOKEN_SECURITY_SCHEME.name)
                        .addList(OpenApiConfig.REFRESH_TOKEN_SECURITY_SCHEME.name)
                )
            }
            operation
        }
}
