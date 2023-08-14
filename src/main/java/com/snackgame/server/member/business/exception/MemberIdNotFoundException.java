package com.snackgame.server.member.business.exception;

public class MemberIdNotFoundException extends MemberException {

    public MemberIdNotFoundException() {
        super("없는 사용자입니다");
    }
}
