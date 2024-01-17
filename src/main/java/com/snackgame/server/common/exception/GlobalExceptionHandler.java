package com.snackgame.server.common.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.snackgame.server.auth.exception.AuthException;
import com.snackgame.server.common.exception.dto.ExceptionResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleUnhandled(Exception exception) {
        log.error(exception.getMessage(), exception);

        return ExceptionResponse.withDefaultMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handleAuthenticationException(AuthException exception) {
        log.warn(exception.getMessage(), exception);

        return new ExceptionResponse(exception.getAction(), exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBusinessException(BusinessException exception) {
        log.debug(exception.getMessage(), exception);

        return ExceptionResponse.from(exception);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNoHandlerFound(NoHandlerFoundException exception) {
        log.debug(exception.getMessage(), exception);

        return new ExceptionResponse("리소스를 찾지 못했습니다");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleMissingRequestParameter(MissingServletRequestParameterException exception) {
        log.debug(exception.getMessage(), exception);

        return new ExceptionResponse("쿼리스트링 '" + exception.getParameterName() + "'이 없거나 잘못됐습니다");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        log.debug(exception.getMessage(), exception);

        return new ExceptionResponse("요청 페이로드를 읽지 못했습니다");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleHttpMessageTypeMismatch(MethodArgumentTypeMismatchException exception) {
        log.debug(exception.getMessage(), exception);

        return new ExceptionResponse("요청 인자가 잘못되었습니다");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        log.debug(exception.getMessage(), exception);

        List<String> exceptionMessages = exception.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return new ExceptionResponse(exceptionMessages);
    }
}
