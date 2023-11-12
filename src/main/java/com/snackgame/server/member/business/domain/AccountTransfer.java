package com.snackgame.server.member.business.domain;

public interface AccountTransfer {

    void transferAll(Member victim, Member newMember);
}
