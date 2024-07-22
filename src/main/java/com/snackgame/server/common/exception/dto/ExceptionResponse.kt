package com.snackgame.server.common.exception.dto

import org.springframework.http.HttpStatus

data class ExceptionResponse(
    val messages: List<String> = DEFAULT_MESSAGE,
    val code: String = DEFAULT_CODE,
) {
    constructor(messages: List<String>, throwableClazz: Class<Throwable>) : this(
        messages,
        throwableClazz.simpleName.upperSnakeCase()
    )

    constructor(message: String, throwableClazz: Class<Throwable>) : this(
        listOf(message),
        throwableClazz
    )

    companion object {
        private val DEFAULT_MESSAGE = listOf("처리 중 예외가 발생했습니다")
        private val DEFAULT_CODE = HttpStatus.INTERNAL_SERVER_ERROR.name
    }
}

private fun String.upperSnakeCase(): String {
    return buildString {
        for (char in this@upperSnakeCase) {
            if (char.isUpperCase() && this.isNotEmpty()) {
                append('_')
            }
            append(char.uppercaseChar())
        }
    }
}
