package com.snackgame.server.member.business.exception;

public class NameLengthException extends MemberException {

    public NameLengthException() {
        super("이름은 2글자 이상이어야 합니다");
    }
}
