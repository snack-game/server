package com.snackgame.server.member.exception;

public class MemberNotFoundException extends MemberException {

    public MemberNotFoundException() {
        super("없는 사용자입니다");
    }
}
