package com.snackgame.server.member.business.exception;

public abstract class MemberException extends RuntimeException {

    public MemberException(String message) {
        super(message);
    }
}
