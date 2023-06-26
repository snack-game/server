package com.snackgame.server.member.business.exception;

import com.snackgame.server.common.exception.BusinessException;

public abstract class MemberException extends BusinessException {

    public MemberException(String message) {
        super(message);
    }
}
