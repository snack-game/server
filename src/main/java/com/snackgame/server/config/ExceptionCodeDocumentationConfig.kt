package com.snackgame.server.config

import com.snackgame.server.common.exception.dto.ExceptionResponse
import io.github.classgraph.ClassGraph
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("dev", "local")
@Configuration
class ExceptionCodeDocumentationConfig {

    @Bean
    fun exceptionCodes(): GroupedOpenApi {
        val throwables = findThrowablesIn("com.snackgame.server")
        val pathItems = throwables.entries.associate { (key, value) ->
            val responses = value.associate { throwable ->
                val throwableInstance = throwable.declaredConstructors
                    .filter { it.parameterCount == 0 }
                    .mapNotNull { it.newInstance() }
                    .map { it as Throwable }
                    .firstOrNull()
                val exceptionResponse = ExceptionResponse(throwableInstance?.message ?: "예외 메시지가 가변적입니다", throwable)
                val response = ApiResponse().content(
                    Content()
                        .addMediaType("application/json", MediaType().example(exceptionResponse))
                )
                exceptionResponse.code to response
            }
            val apiResponses = ApiResponses().also { it.putAll(responses) }
            key to PathItem().also {
                it.operation(
                    PathItem.HttpMethod.GET,
                    Operation().responses(apiResponses)
                        .addTagsItem(key)
                )
            }
        }
        return GroupedOpenApi.builder()
            .group("99-exception-codes")
            .displayName("예외 코드")
            .addOpenApiCustomiser { openApi: OpenAPI ->
                openApi.paths(Paths())
                pathItems.forEach { openApi.path(it.key, it.value) }
            }
            .build()
    }

    fun findThrowablesIn(packageName: String): Map<String, List<Class<Throwable>>> {
        val classes = mutableMapOf<String, MutableList<Class<Throwable>>>()

        val scanResult = ClassGraph()
            .enableAllInfo()
            .acceptPackages(packageName)
            .scan()

        val throwableClasses = scanResult.getSubclasses(RuntimeException::class.java)

        for (classInfo in throwableClasses) {
            if (!classInfo.isAbstract) {
                val clazz = classInfo.loadClass(Throwable::class.java)
                val simplePackageName = classInfo.packageName.substringAfter("$packageName.")
                classes.putIfAbsent(simplePackageName, mutableListOf())
                classes[simplePackageName]?.add(clazz)
            }
        }

        return classes
    }
}
