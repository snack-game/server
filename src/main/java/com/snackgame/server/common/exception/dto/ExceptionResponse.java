package com.snackgame.server.common.exception.dto;

import java.util.List;

public class ExceptionResponse {

    private static final String DEFAULT_MESSAGE = "처리 중 예외가 발생했습니다";

    private final String action;
    private final List<String> messages;

    public ExceptionResponse(String message) {
        this.action = null;
        this.messages = List.of(message);
    }

    public ExceptionResponse(List<String> messages) {
        this.action = null;
        this.messages = messages;
    }

    public ExceptionResponse(String action, String message) {
        this.action = action;
        this.messages = List.of(message);
    }

    public static ExceptionResponse withDefaultMessage() {
        return new ExceptionResponse(DEFAULT_MESSAGE);
    }

    public static ExceptionResponse from(Exception exception) {
        return new ExceptionResponse(exception.getMessage());
    }

    public String getAction() {
        return action;
    }

    public List<String> getMessages() {
        return messages;
    }
}
