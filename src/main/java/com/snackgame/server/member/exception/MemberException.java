package com.snackgame.server.member.exception;

public abstract class MemberException extends RuntimeException {

    public MemberException(String message) {
        super(message);
    }
}
