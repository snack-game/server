package com.snackgame.server.config

import com.snackgame.server.auth.oauth.support.JustAuthenticated
import com.snackgame.server.auth.token.support.Authenticated
import com.snackgame.server.auth.token.support.TokensFromCookie
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.GroupedOpenApi
import org.springdoc.core.SpringDocUtils
import org.springdoc.core.customizers.OpenApiCustomiser
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.HandlerMethod
import kotlin.collections.set

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
                    .addSecuritySchemes(ACCESS_TOKEN_SECURITY_KEY, accessTokenSecurityScheme())
                    .addSecuritySchemes(REFRESH_TOKEN_SECURITY_KEY, refreshTokenSecurityScheme())
            )
            .addServersItem(Server().url("/"))
    }

    @Bean
    fun userApis(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("1-general")
            .displayName("일반")
            .pathsToExclude("/tokens/**", "/games/**")
            .addOpenApiCustomiser(filterDeprecated(false))
            .build()
            .addAllOperationCustomizer(listOf(accessTokenOperationMarker(), refreshTokenOperationMarker()))
    }

    @Bean
    fun gameApis(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("2-game")
            .displayName("게임")
            .pathsToMatch("/games/**")
            .addOpenApiCustomiser(filterDeprecated(false))
            .build()
            .addAllOperationCustomizer(listOf(accessTokenOperationMarker(), refreshTokenOperationMarker()))
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
            .addOpenApiCustomiser(filterDeprecated(false))
            .build()
            .addAllOperationCustomizer(listOf(accessTokenOperationMarker(), refreshTokenOperationMarker()))
    }

    @Bean
    fun deprecatedApis(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("98-deprecated")
            .displayName("Deprecated")
            .addOpenApiCustomiser(filterDeprecated(true))
            .build()
            .addAllOperationCustomizer(listOf(accessTokenOperationMarker(), refreshTokenOperationMarker()))
    }

    @Bean
    fun authCustomPaths(): Paths = Paths()

    private fun accessTokenSecurityScheme(): SecurityScheme {
        return SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .`in`(SecurityScheme.In.COOKIE)
            .name("accessToken")
    }

    private fun refreshTokenSecurityScheme(): SecurityScheme {
        return SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .`in`(SecurityScheme.In.COOKIE)
            .name("refreshToken")
    }

    fun accessTokenOperationMarker(): OperationCustomizer =
        OperationCustomizer { operation: Operation, handlerMethod: HandlerMethod ->
            val hasAnnotation = handlerMethod.methodParameters
                .any { it.hasParameterAnnotation(Authenticated::class.java) }
            if (hasAnnotation) {
                operation.addSecurityItem(
                    SecurityRequirement()
                        .addList(ACCESS_TOKEN_SECURITY_KEY)
                )
            }
            operation
        }

    fun refreshTokenOperationMarker(): OperationCustomizer =
        OperationCustomizer { operation: Operation, handlerMethod: HandlerMethod ->
            val hasAnnotation = handlerMethod.methodParameters
                .any { it.hasParameterAnnotation(TokensFromCookie::class.java) }
            if (hasAnnotation) {
                operation.addSecurityItem(
                    SecurityRequirement()
                        .addList(ACCESS_TOKEN_SECURITY_KEY)
                        .addList(REFRESH_TOKEN_SECURITY_KEY)
                )
            }
            operation
        }

    private fun filterDeprecated(isDeprecated: Boolean): OpenApiCustomiser = OpenApiCustomiser { openApi: OpenAPI ->
        val paths = openApi.paths
        paths.forEach { s: String, pathItem: PathItem ->
            val filteredOperations = pathItem.readOperationsMap().entries
                .filter { (it.value.deprecated ?: false) == isDeprecated }

            val filteredPathItem = PathItem()
            filteredOperations.forEach { filteredPathItem.operation(it.key, it.value) }
            paths[s] = filteredPathItem
        }
    }

    companion object {
        private const val ACCESS_TOKEN_SECURITY_KEY = "accessToken"
        private const val REFRESH_TOKEN_SECURITY_KEY = "refreshToken"

        init {
            SpringDocUtils.getConfig()
                .addAnnotationsToIgnore(
                    Authenticated::class.java,
                    JustAuthenticated::class.java,
                    TokensFromCookie::class.java
                )
        }
    }
}
