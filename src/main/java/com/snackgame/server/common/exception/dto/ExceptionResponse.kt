package com.snackgame.server.common.exception.dto

data class ExceptionResponse(
    val messages: List<String> = DEFAULT_MESSAGE,
    val code: String = DEFAULT_CODE,
    @Deprecated("24/7/8 제거 예정") val action: String? = null,
) {
    constructor(messages: List<String>, throwableClazz: Class<Throwable>, action: String? = null) : this(
        messages,
        throwableClazz.simpleName.upperSnakeCase(),
        action
    )

    constructor(message: String, throwableClazz: Class<Throwable>, action: String? = null) : this(
        listOf(message),
        throwableClazz,
        action
    )

    companion object {
        private val DEFAULT_MESSAGE = listOf("처리 중 예외가 발생했습니다")
        private const val DEFAULT_CODE = "INTERNAL"
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
