package com.snackgame.server.member.business.exception;

public class EmptyNameException extends MemberException {

    public EmptyNameException() {
        super("이름은 비워둘 수 없습니다");
    }
}
