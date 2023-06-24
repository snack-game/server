package com.snackgame.server.member.business.exception;

public class DuplicateNameException extends MemberException {

    public DuplicateNameException() {
        super("이미 존재하는 이름입니다");
    }
}
