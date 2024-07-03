package com.snackgame.server.config

import com.snackgame.server.ServerApplication
import com.snackgame.server.common.exception.dto.ExceptionResponse
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
import java.io.File

@Profile("dev")
@Configuration
class ExceptionCodeDocumentationConfig {

    @Bean
    fun exceptionCodes(): GroupedOpenApi {
        val throwables = findThrowablesIn(ServerApplication::class.java.packageName)
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
        val classLoader = Thread.currentThread().contextClassLoader
        val path = packageName.replace('.', '/')
        val resources = classLoader.getResources(path)
        val directories = mutableListOf<File>()

        while (resources.hasMoreElements()) {
            val resource = resources.nextElement()
            directories.add(File(resource.file))
        }

        val classes = mutableMapOf<String, MutableList<Class<Throwable>>>()
        for (directory in directories) {
            val founds = findClassesIn<Throwable>(directory, packageName)
            for (found in founds) {
                val simplePackageName = found.packageName.substringAfter("$packageName.")
                classes.putIfAbsent(simplePackageName, mutableListOf())
                classes[simplePackageName]?.add(found)
            }
        }
        return classes
    }

    private fun <E> findClassesIn(directory: File, packageName: String): List<Class<E>> {
        val classes = mutableListOf<Class<E>>()
        if (!directory.exists()) {
            return classes
        }
        val files = directory.listFiles() ?: return classes
        for (file in files) {
            if (file.isDirectory) {
                assert(!file.name.contains("."))
                classes.addAll(findClassesIn(file, "$packageName.${file.name}"))
            } else if (file.name.endsWith(".class")) {
                val className = "$packageName.${file.name.substring(0, file.name.length - 6)}"
                val clazz = Class.forName(className)
                if (Throwable::class.java.isAssignableFrom(clazz) && !clazz.kotlin.isAbstract) {
                    classes.add(clazz as Class<E>)
                }
            }
        }
        return classes
    }
}
