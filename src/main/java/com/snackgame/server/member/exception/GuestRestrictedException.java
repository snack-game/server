package com.snackgame.server.member.exception;

public class GuestRestrictedException extends MemberException {

    public GuestRestrictedException() {
        super("게스트는 할 수 없는 행동입니다");
    }
}
