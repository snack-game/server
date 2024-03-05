package com.snackgame.server.member.exception;

public class InvalidProfileImageException extends MemberException {
    public InvalidProfileImageException() {
        super("프로필 이미지가 잘못되었습니다");
    }
}
