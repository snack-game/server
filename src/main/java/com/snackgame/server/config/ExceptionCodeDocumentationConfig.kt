package com.snackgame.server.config

import com.snackgame.server.common.exception.BusinessException
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
import java.io.File

@Configuration
class ExceptionCodeDocumentationConfig {

    @Bean
    fun exceptionCodes(): GroupedOpenApi {
        val exceptionClasses = findSubclasses("com.snackgame.server", BusinessException::class.java)
        val pathItems = exceptionClasses.entries.associate { (key, value) ->
            val responses = value.associate { throwableClazz ->
                val instance = throwableClazz.declaredConstructors
                    .filter { it.parameterCount == 0 }
                    .mapNotNull { it.newInstance() }
                    .map { it as Throwable }
                    .firstOrNull()
                val exceptionResponse = ExceptionResponse(instance?.message ?: "예외 메시지가 가변적입니다", throwableClazz)
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
            .group("4-exception-codes")
            .displayName("예외 코드")
            .addOpenApiCustomiser { openApi: OpenAPI ->
                openApi.paths(Paths())
                pathItems.forEach { openApi.path(it.key, it.value) }
            }
            .build()
    }

    fun findSubclasses(packageName: String, baseClass: Class<*>): Map<String, List<Class<Throwable>>> {
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
            for (clazz in findClasses(directory, packageName)) {
                val simplePackageName = clazz.packageName.substringAfter("$packageName.")
                classes.putIfAbsent(simplePackageName, mutableListOf())
                classes[simplePackageName]?.add(clazz)
            }
        }
        return classes
    }

    private fun findClasses(directory: File, packageName: String): List<Class<Throwable>> {
        val classes = mutableListOf<Class<Throwable>>()
        if (!directory.exists()) {
            return classes
        }
        val files = directory.listFiles() ?: return classes
        for (file in files) {
            if (file.isDirectory) {
                assert(!file.name.contains("."))
                classes.addAll(findClasses(file, "$packageName.${file.name}"))
            } else if (file.name.endsWith(".class")) {
                val className = "$packageName.${file.name.substring(0, file.name.length - 6)}"
                val clazz = Class.forName(className)
                if (Throwable::class.java.isAssignableFrom(clazz) && !clazz.kotlin.isAbstract) {
                    classes.add(clazz as Class<Throwable>)
                }
            }
        }
        return classes
    }
}
