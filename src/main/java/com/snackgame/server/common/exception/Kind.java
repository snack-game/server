package com.snackgame.server.common.exception;

import org.springframework.http.HttpStatus;

public enum Kind {

    INTERNAL_SERVER_ERROR(true, HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(true, HttpStatus.BAD_REQUEST);

    private final boolean needsMessageToBeHidden;
    private final HttpStatus httpStatus;

    Kind(boolean needsMessageToBeHidden, HttpStatus httpStatus) {
        this.needsMessageToBeHidden = needsMessageToBeHidden;
        this.httpStatus = httpStatus;
    }

    public boolean needsMessageToBeHidden() {
        return needsMessageToBeHidden;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
