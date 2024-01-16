package com.snackgame.server.common.exception.dto;

import java.util.List;

public class ExceptionResponse {

    private static final String DEFAULT_MESSAGE = "처리 중 예외가 발생했습니다";

    private final List<String> messages;


    private ExceptionResponse(List<String> messages) {
        this.messages = messages;
    }

    public static ExceptionResponse withoutMessage() {
        return ExceptionResponse.of(DEFAULT_MESSAGE);
    }

    public static ExceptionResponse withMessageOf(Exception exception) {
        return ExceptionResponse.of(exception.getMessage());
    }

    public static ExceptionResponse of(String message) {
        return new ExceptionResponse(List.of(message));
    }

    public static ExceptionResponse of(List<String> messages) {
        return new ExceptionResponse(messages);
    }

    public List<String> getMessages() {
        return messages;
    }
}
