package com.snackgame.server.member.exception;

public class NameLengthException extends MemberException {

    public NameLengthException() {
        super("이름은 2글자 이상 16글자 이하여야 합니다");
    }
}
