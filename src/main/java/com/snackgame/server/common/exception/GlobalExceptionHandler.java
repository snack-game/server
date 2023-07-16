package com.snackgame.server.common.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.snackgame.server.auth.exception.AuthorizationException;
import com.snackgame.server.common.exception.dto.ExceptionResponse;
import com.snackgame.server.member.business.exception.MemberNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleUnhandled(Exception exception) {
        logger.error(exception.getMessage(), exception.getCause());
        if (logger.isDebugEnabled()) {
            exception.printStackTrace();
        }
        return ExceptionResponse.withoutMessage();
    }

    @ExceptionHandler({AuthorizationException.class, MemberNotFoundException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handleAuthenticationException(AuthorizationException exception) {
        logger.info(exception.getMessage(), exception.getCause());
        return ExceptionResponse.withMessageOf(exception);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBusinessException(BusinessException exception) {
        logger.debug(exception.getMessage(), exception.getCause());
        return ExceptionResponse.withMessageOf(exception);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleMissingRequestParameter(MissingServletRequestParameterException exception) {
        logger.debug(exception.getMessage(), exception.getCause());
        return ExceptionResponse.of("쿼리스트링 '" + exception.getParameterName() + "'이 없거나 잘못됐습니다");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        logger.debug(exception.getMessage(), exception.getCause());
        return ExceptionResponse.of("요청 페이로드를 읽지 못했습니다");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        logger.debug(exception.getMessage(), exception.getCause());
        List<String> exceptionMessages = exception.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return ExceptionResponse.of(exceptionMessages);
    }
}
