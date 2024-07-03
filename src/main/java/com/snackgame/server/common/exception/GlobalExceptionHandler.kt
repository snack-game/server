package com.snackgame.server.common.exception

import com.snackgame.server.auth.exception.AuthException
import com.snackgame.server.common.exception.dto.ExceptionResponse
import com.snackgame.server.common.exception.event.ExceptionalEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import java.util.stream.Collectors

@RestControllerAdvice
class GlobalExceptionHandler(
    private val eventPublisher: ApplicationEventPublisher
) {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleUnhandled(exception: Exception): ExceptionResponse {
        log.error(exception.message, exception)
        eventPublisher.publishEvent(ExceptionalEvent(exception.javaClass.simpleName, exception.message!!))

        return ExceptionResponse()
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAuthenticationException(exception: AuthException): ExceptionResponse {
        log.warn(exception.message, exception)

        return ExceptionResponse(exception.message!!, exception.javaClass, exception.action)
    }

    @ExceptionHandler
    fun handleBusinessException(exception: BusinessException): ResponseEntity<ExceptionResponse> {
        val kind = exception.kind
        return with(ResponseEntity.status(kind.httpStatus)) {
            if (kind.needsMessageToBeHidden()) {
                log.error(exception.message, exception)
                eventPublisher.publishEvent(ExceptionalEvent(exception.javaClass.simpleName, exception.message!!))
                return body(ExceptionResponse())
            }
            log.debug(exception.message, exception)
            body(ExceptionResponse(exception.message!!, exception.javaClass))
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNoHandlerFound(exception: NoHandlerFoundException): ExceptionResponse {
        log.debug(exception.message, exception)

        return ExceptionResponse("리소스를 찾지 못했습니다", exception.javaClass)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    fun handleMissingRequestParameter(exception: HttpRequestMethodNotSupportedException): ExceptionResponse {
        log.debug(exception.message, exception)

        return ExceptionResponse("HTTP 메서드가 잘못되었습니다", exception.javaClass)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMissingRequestParameter(exception: MissingServletRequestParameterException): ExceptionResponse {
        log.debug(exception.message, exception)

        return ExceptionResponse("쿼리스트링 '${exception.parameterName}'이 없거나 잘못됐습니다", exception.javaClass)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMessageNotReadable(exception: HttpMessageNotReadableException): ExceptionResponse {
        log.debug(exception.message, exception)

        return ExceptionResponse("요청 페이로드를 읽지 못했습니다", exception.javaClass)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMessageTypeMismatch(exception: MethodArgumentTypeMismatchException): ExceptionResponse {
        log.debug(exception.message, exception)

        return ExceptionResponse("요청 인자가 잘못되었습니다", exception.javaClass)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValid(exception: MethodArgumentNotValidException): ExceptionResponse {
        log.debug(exception.message, exception)

        val exceptionMessages = exception.fieldErrors.stream()
            .map { obj: FieldError -> obj.defaultMessage }
            .collect(Collectors.toList())

        return ExceptionResponse(exceptionMessages, exception.javaClass)
    }

    companion object {
        private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }
}
