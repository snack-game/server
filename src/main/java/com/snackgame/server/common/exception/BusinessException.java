package com.snackgame.server.common.exception;

public abstract class BusinessException extends RuntimeException {

    private final Kind kind;

    public BusinessException(String message) {
        this(Kind.BAD_REQUEST, message);
    }

    public BusinessException(Kind kind, String message) {
        super(message);
        this.kind = kind;
    }

    public Kind getKind() {
        return kind;
    }
}
